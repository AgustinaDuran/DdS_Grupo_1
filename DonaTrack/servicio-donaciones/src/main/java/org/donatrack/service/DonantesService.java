package org.donatrack.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.donatrack.controller.dto.Donantes.CrearDonanteDTO;
import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.donante.DonanteJuridico;
import org.donatrack.dominio.donante.DonantePersona;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.donatrack.dominio.usuario.persona.Persona;
import org.donatrack.repository.DonantesRepository;
import org.springframework.stereotype.Service;
import org.donatrack.controller.dto.Usuarios.CrearUsuarioDTO;

@Service
public class DonantesService {

    private DonantesRepository donantesRepository;
    private UsuariosService usuariosService;

    public DonantesService(DonantesRepository donantesRepository, UsuariosService usuariosService) {
        this.donantesRepository = donantesRepository;
        this.usuariosService = usuariosService;
    }

    public List<Donante> obtenerDonantes() {
        return donantesRepository.findAll();
    }

    public Optional<Donante> obtenerDonantePorId(Long id) {
        return donantesRepository.findById(id);
    }

    public Donante registrarDonante(CrearDonanteDTO nuevoDonante) {
        System.out.println("Hola2");
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
        return donantesRepository.save(donante);
    }

    // por ahora es simple pero guardarDonante implicará varios chequeos y demás
    public Donante guardarDonante(Donante donante) {
        return donantesRepository.save(donante);
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

    public void eliminarDonante(Long id) {
        donantesRepository.delete(id);
    }

    private DatosUsuario registrarNuevoUsuarioParaDonante(CrearDonanteDTO nuevoDonante) {
        CrearUsuarioDTO crearUsuarioDTO = new CrearUsuarioDTO(nuevoDonante);
        return usuariosService.registrarUsuario(crearUsuarioDTO);        
    }

}
