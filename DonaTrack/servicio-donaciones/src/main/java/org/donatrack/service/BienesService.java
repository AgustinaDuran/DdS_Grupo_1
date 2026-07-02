package org.donatrack.service;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.dominio.bien.Bien;
import org.donatrack.repository.BienesRepository;

public class BienesService {

    private BienesRepository bienesRepository;
    
    public BienesService(BienesRepository bienesRepository) {
        this.bienesRepository = bienesRepository;
    }

    public void crearBien(Bien bien) {
        bienesRepository.save(bien);
    }

    public void eliminarBienPorId(Long id) {
        bienesRepository.delete(id);
    }

    public void actualizarBien(Long id, Bien bienActualizado) {
        Bien bienExistente = bienesRepository.findById(id);
        if (bienExistente != null) {
            bienExistente.setNombre(bienActualizado.getNombre());
            bienExistente.setDescripcion(bienActualizado.getDescripcion());
            bienesRepository.save(bienExistente);
        }

    }

    public List<Bien> obtenerBienes() {
        return bienesRepository.findAll();
    }

    
}