package org.donatrack.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.donatrack.controller.dto.Donantes.CrearDonanteDTO;
import org.donatrack.controller.dto.Donantes.ImportacionCsvResultadoDTO;
import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.donante.DonanteJuridico;
import org.donatrack.dominio.donante.DonantePersona;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.donatrack.dominio.usuario.persona.Persona;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.repository.DonantesRepository;
import org.springframework.stereotype.Service;
import org.donatrack.controller.dto.Usuarios.CrearUsuarioDTO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.annotation.Lazy;
import org.donatrack.integracion.KeycloakUserClient;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonantesService {

    private DonantesRepository donantesRepository;
    private UsuariosService usuariosService;

    private final ImportacionDonantesService importacionDonantesService;
    private final KeycloakUserClient keycloakUserClient;

    public DonantesService(DonantesRepository donantesRepository, UsuariosService usuariosService,
                           @Lazy ImportacionDonantesService importacionDonantesService,
                           KeycloakUserClient keycloakUserClient) {
        this.donantesRepository = donantesRepository;
        this.usuariosService = usuariosService;
        this.importacionDonantesService = importacionDonantesService;
        this.keycloakUserClient = keycloakUserClient;
    }

    public List<Donante> obtenerDonantes() {
        return donantesRepository.findAll();
    }

    public Optional<Donante> obtenerDonantePorId(Long id) {
        return donantesRepository.findById(id);
    }

    @Transactional
    public Donante registrarDonante(CrearDonanteDTO nuevoDonante) {
        boolean usuarioNuevo = nuevoDonante.getUsuarioId() == null;
        String keycloakUserId = null;
        if (usuarioNuevo) {
            String email = emailDe(nuevoDonante);
            validarCuentaNueva(email, nuevoDonante.getContrasenaTemporal());
            if (buscarPorEmail(email).isPresent()) {
                throw new IllegalArgumentException("Ya existe un donante registrado con ese correo.");
            }
            keycloakUserId = keycloakUserClient.crearDonante(email,
                    nuevoDonante.getNombre() == null ? nuevoDonante.getRazonSocial() : nuevoDonante.getNombre(),
                    nuevoDonante.getApellido(), nuevoDonante.getContrasenaTemporal());
        }
        try {
            DatosUsuario datosUsuario;
            Long id = nuevoDonante.getUsuarioId();
            if (id != null) {
                datosUsuario = usuariosService.obtenerUsuarioPorId(id);
                if (datosUsuario == null) throw new IllegalArgumentException("El usuario con ID " + id + " no existe.");
            } else {
                datosUsuario = registrarNuevoUsuarioParaDonante(nuevoDonante);
            }
            Donante donante = switch (nuevoDonante.getTipo()) {
                case JURIDICA -> new DonanteJuridico(datosUsuario, nuevoDonante.getTipoPersonaJuridica(), nuevoDonante.getRubro());
                case HUMANA -> new DonantePersona(datosUsuario);
            };
            return donantesRepository.save(donante);
        } catch (RuntimeException exception) {
            if (usuarioNuevo) keycloakUserClient.eliminarCuenta(keycloakUserId);
            throw exception;
        }
    }

    // por ahora es simple pero guardarDonante implicará varios chequeos y demás
    public Donante guardarDonante(Donante donante) {
        return donantesRepository.save(donante);
    }

    /** Alias de guardarDonante que respeta el nombre "actualizarDonante" del diagrama. */
    public Donante actualizarDonante(Donante donante) {
        return guardarDonante(donante);
    }

    public ImportacionCsvResultadoDTO importarDonantesMasivo(MultipartFile archivo) {
    return importacionDonantesService.generarDonantesDesdeCSV(archivo);
}

    /**
     * Índice email (normalizado) -> Donante, construido en una sola pasada. Lo usa la
     * importación masiva por CSV para resolver el upsert por email en O(1) por fila,
     * evitando un escaneo lineal por cada registro del archivo.
     */
    public Map<String, Donante> indexarPorEmail() {
        Map<String, Donante> indice = new HashMap<>();
        for (Donante donante : donantesRepository.findAll()) {
            String email = emailDe(donante.getDatosUsuario());
            if (email != null) {
                indice.putIfAbsent(email, donante);
            }
        }
        return indice;
    }

    public Optional<Donante> buscarPorEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        String normalizado = email.trim().toLowerCase();
        return donantesRepository.findAll().stream()
                .filter(d -> normalizado.equals(emailDe(d.getDatosUsuario())))
                .findFirst();
    }

    /**
     * Devuelve el email de un usuario en minúsculas, buscándolo entre sus contactos (medio
     * "MAIL") y, para personas humanas, en su contacto predeterminado. {@code null} si no tiene.
     */
    public static String emailDe(DatosUsuario datos) {
        if (datos == null) {
            return null;
        }
        if (datos.getContactos() != null) {
            for (Contacto contacto : datos.getContactos()) {
                if (esEmail(contacto)) {
                    return contacto.getValor().trim().toLowerCase();
                }
            }
        }
        if (datos instanceof Persona persona && esEmail(persona.getContactoPredeterminado())) {
            return persona.getContactoPredeterminado().getValor().trim().toLowerCase();
        }
        return null;
    }

    private static boolean esEmail(Contacto contacto) {
        return contacto != null
                && contacto.getValor() != null
                && "MAIL".equalsIgnoreCase(contacto.getMedio());
    }

    public List<Donante> buscarDonantes(String q) {
        if (q == null || q.isBlank()) {
            return obtenerDonantes();
        }
        String termino = q.trim().toLowerCase();
        List<Donante> encontrados = new ArrayList<>();
        for (Donante donante : donantesRepository.findAll()) {
            if (coincide(donante.getDatosUsuario(), termino)) {
                encontrados.add(donante);
                if (encontrados.size() == 20) {
                    break;
                }
            }
        }
        return encontrados;
    }

    private static boolean coincide(DatosUsuario datos, String termino) {
        List<String> campos = new ArrayList<>();
        if (datos instanceof Persona persona) {
            campos.add(persona.getNombre());
            campos.add(persona.getApellido());
            campos.add(persona.getDni());
            campos.add(((persona.getNombre() == null ? "" : persona.getNombre()) + " "
                    + (persona.getApellido() == null ? "" : persona.getApellido())).trim());
        } else if (datos instanceof Organizacion organizacion) {
            campos.add(organizacion.getRazonSocial());
            campos.add(organizacion.getCuit());
        }
        campos.add(emailDe(datos));
        return campos.stream().anyMatch(c -> c != null && c.toLowerCase().contains(termino));
    }

    public void eliminarDonante(Long id) {
        donantesRepository.deleteById(id);
    }

    /** Alias de eliminarDonante que respeta el nombre "darBajaDonante" del diagrama. */
    public void darBajaDonante(Long id) {
        eliminarDonante(id);
    }

    private DatosUsuario registrarNuevoUsuarioParaDonante(CrearDonanteDTO nuevoDonante) {
        CrearUsuarioDTO crearUsuarioDTO = new CrearUsuarioDTO(nuevoDonante);
        return usuariosService.registrarUsuario(crearUsuarioDTO);        
    }

    private static String emailDe(CrearDonanteDTO donante) {
        return donante.getContactoDTOPredeterminado() == null
                ? null
                : donante.getContactoDTOPredeterminado().getValor();
    }

    private static void validarCuentaNueva(String email, String contrasenaTemporal) {
        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("El correo de contacto no es válido.");
        }
        if (contrasenaTemporal == null || contrasenaTemporal.length() < 8) {
            throw new IllegalArgumentException("La contraseña temporal debe tener al menos 8 caracteres.");
        }
    }

}
