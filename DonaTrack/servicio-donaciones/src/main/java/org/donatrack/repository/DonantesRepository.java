package org.donatrack.repository;

import java.util.List;

import org.donatrack.dominio.donante.Donante;
import java.util.Optional;
import java.util.ArrayList;

public class DonantesRepository {
    private final List<Donante> donantes = new ArrayList<>();

    public List<Donante> findAll() {
        return new ArrayList<>(donantes);
    }

    public void agregarDonantes(List<Donante> donantes){
        this.donantes.addAll(donantes);
    }

    public Optional<Donante> findById(long id) {
        return donantes.stream()
                .filter(d -> d.getId() == id)
                .findFirst();
    }

    public Donante save(Donante donante){
        donantes.add(donante);
        return donante;
    }

    public void delete(long id){
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
    public Optional<Donante> findById(long id) {
        return donantes.stream().filter(d -> d.getId() == id).findFirst();
    }
    public Donante save(Donante donante){ donantes.add(donante); return donante; }
    public void delete(long id){ donantes.removeIf(d -> d.getId() == id); }
}
*/