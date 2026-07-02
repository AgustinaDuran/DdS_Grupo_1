package org.donatrack.service;

import java.util.List;
import java.util.Optional;

import org.donatrack.controller.dto.Donantes.CrearDonanteDTO;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.donante.DonanteJuridico;
import org.donatrack.dominio.donante.DonantePersona;
import org.donatrack.dominio.usuario.DatosUsuario;
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
            datosUsuario = registrarNuevoUsuarioParaDonante(nuevoDonante);
        }

        Donante donante;

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

    public void eliminarDonante(Long id) {
        donantesRepository.delete(id);
    }

    private DatosUsuario registrarNuevoUsuarioParaDonante(CrearDonanteDTO nuevoDonante) {
        CrearUsuarioDTO crearUsuarioDTO = new CrearUsuarioDTO(nuevoDonante);
        return usuariosService.registrarUsuario(crearUsuarioDTO);        
    }

}
