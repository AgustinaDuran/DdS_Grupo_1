package org.donatrack.service;

import org.donatrack.controller.dto.Usuarios.CrearUsuarioDTO;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.repository.UsuariosRepository;
import org.donatrack.dominio.usuario.persona.Persona;

public class UsuariosService {
    UsuariosRepository usuariosRepository;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    public DatosUsuario registrarUsuario(CrearUsuarioDTO crearUsuarioDTO) {
        DatosUsuario nuevoUsuario;
        switch (crearUsuarioDTO.getRolUsuario()) {
            case PERSONA:
                if (crearUsuarioDTO.getNombre() == null || crearUsuarioDTO.getApellido() == null) {
                    throw new IllegalArgumentException("Nombre y apellido son obligatorios para el rol PERSONA");
                }
                nuevoUsuario = new Persona(
                    crearUsuarioDTO.getNombre(), crearUsuarioDTO.getApellido(),
                    crearUsuarioDTO.getEdad(), crearUsuarioDTO.getDni(),
                    crearUsuarioDTO.getGenero(), crearUsuarioDTO.getDireccion(),
                    crearUsuarioDTO.getContactoPredeterminado()
                );
                break;
            case ORGANIZACION:
                if (crearUsuarioDTO.getRazonSocial() == null || crearUsuarioDTO.getCuit() == null) {
                    throw new IllegalArgumentException("Razón social y CUIT son obligatorios para el rol ORGANIZACION");
                }
                nuevoUsuario = new Organizacion(
                    crearUsuarioDTO.getRazonSocial(), crearUsuarioDTO.getCuit()
                );
                break;
            default:
                throw new IllegalArgumentException("Rol de usuario no válido");
        }
        return usuariosRepository.save(nuevoUsuario);
    }

    public DatosUsuario obtenerUsuarioPorId(Long id) {
        return usuariosRepository.findById(id).orElse(null);
    }

    public DatosUsuario actualizarUsuario(Long id, CrearUsuarioDTO crearUsuarioDTO) {
        DatosUsuario usuarioExistente = usuariosRepository.findById(id).orElse(null);
        if (usuarioExistente == null) {
            throw new IllegalArgumentException("El usuario con ID " + id + " no existe.");
        }

        switch (crearUsuarioDTO.getRolUsuario()) {
            case PERSONA:
                if (!(usuarioExistente instanceof Persona persona)) {
                    throw new IllegalArgumentException("El usuario con ID " + id + " no es del tipo PERSONA.");
                }
                persona.setNombre(crearUsuarioDTO.getNombre());
                persona.setApellido(crearUsuarioDTO.getApellido());
                persona.setEdad(crearUsuarioDTO.getEdad());
                persona.setDni(crearUsuarioDTO.getDni());
                persona.setGenero(crearUsuarioDTO.getGenero());
                persona.setDireccion(crearUsuarioDTO.getDireccion());
                persona.setContactoPredeterminado(crearUsuarioDTO.getContactoPredeterminado());
                break;
            case ORGANIZACION:
                if (!(usuarioExistente instanceof Organizacion organizacion)) {
                    throw new IllegalArgumentException("El usuario con ID " + id + " no es del tipo ORGANIZACION.");
                }
                organizacion.setRazonSocial(crearUsuarioDTO.getRazonSocial());
                organizacion.setCuit(crearUsuarioDTO.getCuit());
                break;
            default:
                throw new IllegalArgumentException("Rol de usuario no válido");
        }

        return usuariosRepository.save(usuarioExistente);
    }

}
