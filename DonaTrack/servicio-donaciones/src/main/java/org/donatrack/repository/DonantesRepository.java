package org.donatrack.repository;

import java.util.List;

import org.donatrack.dominio.donante.Donante;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

@Repository
public class DonantesRepository {
    private final List<Donante> donantes = new ArrayList<>();
    private Long nextId = 1L;

    public List<Donante> findAll() {
        return new ArrayList<>(donantes);
    }

    public void agregarDonantes(List<Donante> donantes){
        this.donantes.addAll(donantes);
    }

    public Optional<Donante> findById(Long id) {
        return donantes.stream()
                .filter(d -> d.getId() == id)
                .findFirst();
    }

    public Donante save(Donante donante){
        if (donante.getId() == null) {
            donante.setId(nextId++);
        } else {
            donantes.removeIf(existente -> donante.getId().equals(existente.getId()));
        }
        donantes.add(donante);
        return donante;
    }

    public void delete(Long id){
        donantes.removeIf(d -> d.getId() == id);
    }

}

/*
// Variante como bean de Spring en memoria (para que funcione la inyección por constructor)
package org.donatrack.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.donatrack.dominio.donante.Donante;
import org.springframework.stereotype.Repository;

@Repository
public class DonantesRepository {
    private final List<Donante> donantes = new ArrayList<>();

    public List<Donante> findAll() { return new ArrayList<>(donantes); }
    public void agregarDonantes(List<Donante> donantes){ this.donantes.addAll(donantes); }
    public Optional<Donante> findById(Long id) {
        return donantes.stream().filter(d -> d.getId() == id).findFirst();
    }
    public Donante save(Donante donante){ donantes.add(donante); return donante; }
    public void delete(Long id){ donantes.removeIf(d -> d.getId() == id); }
}
*/