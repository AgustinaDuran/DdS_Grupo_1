package org.donatrack.service;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.repository.EntidadesBeneficiariasRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EntidadesBeneficiariasService {

    private EntidadesBeneficiariasRepository entidadesBeneficiariasRepository;

    public EntidadesBeneficiariasService(EntidadesBeneficiariasRepository donantesRepository) {
        this.entidadesBeneficiariasRepository = entidadesBeneficiariasRepository;
    }

    public List<EntidadBeneficiaria> obtenerEntidades() {
        return entidadesBeneficiariasRepository.findAll();
    }

    public EntidadBeneficiaria nuevaEntidad(EntidadBeneficiaria entidad) {
        return entidadesBeneficiariasRepository.save(entidad);
    }

    public void eliminarEntidad(Long id) {
        entidadesBeneficiariasRepository.delete(id);
    }
    
}