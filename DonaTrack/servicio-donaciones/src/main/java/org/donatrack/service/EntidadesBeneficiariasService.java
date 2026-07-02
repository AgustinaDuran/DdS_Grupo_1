package org.donatrack.service;

import org.donatrack.controller.dto.EntidadBeneficiaria.CrearEntidadBeneficiariaDTO;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.repository.EntidadesBeneficiariasRepository;
import org.springframework.stereotype.Service;
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
        return entidadesBeneficiariasRepository.findById(id);
    }

    public EntidadBeneficiaria registrarEntidad(CrearEntidadBeneficiariaDTO nuevaEntidad) {
        EntidadBeneficiaria entidad = new EntidadBeneficiaria(
                nuevaEntidad.getTipoEntidad(),
                nuevaEntidad.getDireccion());
        return entidadesBeneficiariasRepository.save(entidad);
    }

    public void eliminarEntidad(Long id) {
        entidadesBeneficiariasRepository.delete(id);
    }

}
