package org.donatrack.service;

import org.donatrack.controller.dto.EntidadBeneficiaria.CrearEntidadBeneficiariaDTO;
import org.donatrack.controller.dto.EntidadBeneficiaria.ActualizarEntidadBeneficiariaDTO;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.repository.EntidadesBeneficiariasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EntidadesBeneficiariasService {

    private final EntidadesBeneficiariasRepository entidadesBeneficiariasRepository;

    public EntidadesBeneficiariasService(EntidadesBeneficiariasRepository entidadesBeneficiariasRepository) {
        this.entidadesBeneficiariasRepository = entidadesBeneficiariasRepository;
    }

    public List<EntidadBeneficiaria> obtenerEntidades() {
        return entidadesBeneficiariasRepository.findAll();
    }

    public EntidadBeneficiaria obtenerEntidadPorId(Long id) {
        return entidadesBeneficiariasRepository.findById(id).orElse(null);
    }

    public EntidadBeneficiaria registrarEntidad(CrearEntidadBeneficiariaDTO nuevaEntidad) {
        Organizacion organizacion = nuevaEntidad.getOrganizacion().toOrganizacion();
        EntidadBeneficiaria entidad = new EntidadBeneficiaria(
                nuevaEntidad.getTipoEntidad(),
                nuevaEntidad.getDireccion(),
                organizacion,
                nuevaEntidad.getKeycloakUserId());
        return entidadesBeneficiariasRepository.save(entidad);
    }

    public EntidadBeneficiaria obtenerPorIdentidad(String keycloakUserId) {
        return entidadesBeneficiariasRepository.findByKeycloakUserId(keycloakUserId).orElse(null);
    }
    @Transactional
    public EntidadBeneficiaria actualizarEntidad(Long id, ActualizarEntidadBeneficiariaDTO datosActualizacion) {
        EntidadBeneficiaria entidad = entidadesBeneficiariasRepository.findById(id).orElse(null);
        if (entidad == null) {
            return null;
        }
        if (datosActualizacion.getTipoEntidad() != null) {
            entidad.setTipoEntidad(datosActualizacion.getTipoEntidad());
        }
        if (datosActualizacion.getDireccion() != null) {
            entidad.setDireccion(datosActualizacion.getDireccion());
        }
        if (entidad.getOrganizacion() != null) {
            Organizacion organizacion = entidad.getOrganizacion();
            if (datosActualizacion.getRazonSocial() != null) {
                organizacion.setRazonSocial(datosActualizacion.getRazonSocial());
            }
            if (datosActualizacion.getCuit() != null) {
                organizacion.setCuit(datosActualizacion.getCuit());
            }
        } else if (datosActualizacion.getRazonSocial() != null || datosActualizacion.getCuit() != null) {
            throw new IllegalArgumentException("La entidad no tiene una organización asociada para actualizar.");
        }
        return entidadesBeneficiariasRepository.save(entidad);
    }

    public void eliminarEntidad(Long id) {
        entidadesBeneficiariasRepository.deleteById(id);
    }

}
