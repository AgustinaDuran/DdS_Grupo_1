package org.donatrack.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.donatrack.dominio.bien.Bien;
import org.springframework.stereotype.Repository;

@Repository
public class BienesRepository {
    public List<Bien> bienes;
    private Long nextId = 1L;

    public BienesRepository() {
        bienes = new ArrayList<>();
    }
    
    public void save(Bien bien) {
        if (bien.getId() == null) {
            bien.setId(nextId++);
        }
        bienes.add(bien);
    }

    public void agregarBienes(List<Bien> bienes) {
        this.bienes.addAll(bienes);
    }

    public void delete(Long id) {
        bienes = bienes.stream()
                .filter(bien -> bien.getId() != id)
                .collect(Collectors.toCollection(ArrayList::new));
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

/*
// Variante como bean de Spring en memoria (para que funcione la inyección por constructor)
package org.donatrack.repository;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.dominio.bien.Bien;
import org.springframework.stereotype.Repository;

@Repository
public class BienesRepository {
    public List<Bien> bienes;

    public BienesRepository() { bienes = new ArrayList<>(); }

    public void save(Bien bien) { bienes.add(bien); }
    public void agregarBienes(List<Bien> bienes) { this.bienes.addAll(bienes); }
    public void delete(Long id) {
        bienes = bienes.stream().filter(bien -> bien.getId() != id).toList();
    }
    public List<Bien> findAll() { return bienes; }
    public Bien findById(Long id) {
        return bienes.stream().filter(bien -> bien.getId() == id).findFirst().orElse(null);
    }
}
*/
