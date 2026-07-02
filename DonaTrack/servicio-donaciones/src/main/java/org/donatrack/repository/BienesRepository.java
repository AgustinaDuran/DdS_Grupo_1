package org.donatrack.repository;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.dominio.bien.Bien;

public class BienesRepository {
    public List<Bien> bienes;

    public BienesRepository() {
        bienes = new ArrayList<>();
    }
    
    public void save(Bien bien) {
        bienes.add(bien);
    }

    public void agregarBienes(List<Bien> bienes) {
        this.bienes.addAll(bienes);
    }

    public void delete(Long id) {
        bienes = bienes.stream()
                .filter(bien -> bien.getId() != id)
                .toList();
    }

    public List<Bien> findAll() {
        return bienes;
    }

    public Bien findById(Long id) {
        return bienes.stream()
                .filter(bien -> bien.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
