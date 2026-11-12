package org.donatrack.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.donatrack.controller.dto.Donantes.CrearDonanteDTO;
import org.donatrack.controller.dto.Donantes.ActualizarDonanteDTO;
import org.donatrack.controller.dto.Donantes.ImportacionCsvResultadoDTO;
import org.donatrack.controller.dto.Donantes.ContactoDTO;
import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.donante.DonanteJuridico;
import org.donatrack.dominio.donante.DonantePersona;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.dominio.usuario.persona.Persona;
import org.donatrack.repository.DonantesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.donatrack.controller.dto.Usuarios.CrearUsuarioDTO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.annotation.Lazy;
import org.donatrack.integracion.DestinatarioResolver;
import org.donatrack.integracion.NotificacionesClient;

@Service
public class DonantesService {

    private DonantesRepository donantesRepository;
    private UsuariosService usuariosService;

    private final ImportacionDonantesService importacionDonantesService;
    private final NotificacionesClient notificacionesClient;
    private final DestinatarioResolver destinatarioResolver;

    public DonantesService(DonantesRepository donantesRepository, UsuariosService usuariosService,
                           @Lazy ImportacionDonantesService importacionDonantesService,
                           NotificacionesClient notificacionesClient,
                           DestinatarioResolver destinatarioResolver) {
        this.donantesRepository = donantesRepository;
        this.usuariosService = usuariosService;
        this.importacionDonantesService = importacionDonantesService;
        this.notificacionesClient = notificacionesClient;
        this.destinatarioResolver = destinatarioResolver;
    }

    public List<Donante> obtenerDonantes() {
        return donantesRepository.findAll();
    }

    public Optional<Donante> obtenerDonantePorId(Long id) {
        return donantesRepository.findById(id);
    }

    public Donante registrarDonante(CrearDonanteDTO nuevoDonante) {
        System.out.println("Hola2");
        boolean usuarioNuevo = nuevoDonante.getUsuarioId() == null;
        DatosUsuario datosUsuario;
        Long id = nuevoDonante.getUsuarioId();
        if (id != null) {
            DatosUsuario usuarioExistente = usuariosService.obtenerUsuarioPorId(id);
            if (usuarioExistente != null) {
                datosUsuario = usuarioExistente;
            } else {
                throw new IllegalArgumentException("El usuario con ID " + id + " no existe.");
            }
        } else{
            System.out.println("Hola3");
            datosUsuario = registrarNuevoUsuarioParaDonante(nuevoDonante);
        }

        Donante donante;
        System.out.println("Hola4");
        switch (nuevoDonante.getTipo()) {
            case JURIDICA:
                donante = new DonanteJuridico(datosUsuario, nuevoDonante.getTipoPersonaJuridica(), nuevoDonante.getRubro());
                break;
            case HUMANA:
                donante = new DonantePersona(datosUsuario);
                break;
            default:
                throw new IllegalArgumentException("Tipo de donante no válido");
        }
        Donante guardado = donantesRepository.save(donante);

        if (usuarioNuevo) {
            notificacionesClient.enviar(destinatarioResolver.paraDonante(
                    guardado,
                    "Bienvenido a DonaTrack. Tu usuario fue registrado correctamente."));
        }
        return guardado;
    }

    // por ahora es simple pero guardarDonante implicará varios chequeos y demás
    public Donante guardarDonante(Donante donante) {
        return donantesRepository.save(donante);
    }

    /** Alias de guardarDonante que respeta el nombre "actualizarDonante" del diagrama. */
    public Donante actualizarDonante(Donante donante) {
        return guardarDonante(donante);
    }

    @Transactional
    public Donante actualizarDonante(Long id, ActualizarDonanteDTO datosActualizacion) {
        Donante donante = donantesRepository.findById(id).orElse(null);
        if (donante == null) {
            return null;
        }
        DatosUsuario usuario = donante.getDatosUsuario();

        if (usuario instanceof Persona persona) {
            if (datosActualizacion.getTipoPersonaJuridica() != null
                    || datosActualizacion.getRubro() != null
                    || datosActualizacion.getRazonSocial() != null
                    || datosActualizacion.getCuit() != null) {
                throw new IllegalArgumentException("Los campos de organización no corresponden a un donante humano.");
            }
            if (datosActualizacion.getNombre() != null) {
                persona.setNombre(datosActualizacion.getNombre());
            }
            if (datosActualizacion.getApellido() != null) {
                persona.setApellido(datosActualizacion.getApellido());
            }
            if (datosActualizacion.getEdad() != null) {
                persona.setEdad(datosActualizacion.getEdad());
            }
            if (datosActualizacion.getDni() != null) {
                persona.setDni(datosActualizacion.getDni());
            }
            if (datosActualizacion.getGenero() != null) {
                persona.setGenero(datosActualizacion.getGenero());
            }
            if (datosActualizacion.getDireccion() != null) {
                persona.setDireccion(datosActualizacion.getDireccion());
            }
            if (datosActualizacion.getContactoPredeterminado() != null) {
                persona.setContactoPredeterminado(contactoDesde(datosActualizacion.getContactoPredeterminado()));
            }
        } else if (usuario instanceof Organizacion organizacion) {
            if (datosActualizacion.getNombre() != null
                    || datosActualizacion.getApellido() != null
                    || datosActualizacion.getEdad() != null
                    || datosActualizacion.getDni() != null
                    || datosActualizacion.getGenero() != null
                    || datosActualizacion.getDireccion() != null
                    || datosActualizacion.getContactoPredeterminado() != null) {
                throw new IllegalArgumentException("Los campos de persona no corresponden a un donante jurídico.");
            }
            if (datosActualizacion.getRazonSocial() != null) {
                organizacion.setRazonSocial(datosActualizacion.getRazonSocial());
            }
            if (datosActualizacion.getCuit() != null) {
                organizacion.setCuit(datosActualizacion.getCuit());
            }
        } else {
            throw new IllegalArgumentException("El donante no tiene un tipo de usuario compatible.");
        }

        if (datosActualizacion.getContactos() != null) {
            usuario.getContactos().clear();
            for (ContactoDTO contacto : datosActualizacion.getContactos()) {
                usuario.agregarContacto(contactoDesde(contacto));
            }
        }

        if (donante instanceof DonanteJuridico donanteJuridico) {
            if (datosActualizacion.getTipoPersonaJuridica() != null) {
                donanteJuridico.setTipoPersonaJuridica(datosActualizacion.getTipoPersonaJuridica());
            }
            if (datosActualizacion.getRubro() != null) {
                donanteJuridico.setRubro(datosActualizacion.getRubro());
            }
        } else if (!(donante instanceof DonantePersona)) {
            throw new IllegalArgumentException("Tipo de donante no compatible.");
        }

        usuariosService.guardarUsuario(usuario);
        return donantesRepository.save(donante);
    }

    private Contacto contactoDesde(ContactoDTO contactoDTO) {
        if (contactoDTO == null) {
            throw new IllegalArgumentException("Los contactos no pueden ser nulos.");
        }
        return new Contacto(contactoDTO.getMedio(), contactoDTO.getValor());
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

}
