package org.donatrack.repository;

import java.util.List;

import java.util.Optional;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

import java.util.ArrayList;

public class EntidadesBeneficiariasRepository {
    private final List<EntidadBeneficiaria> entidades;

    private static EntidadesBeneficiariasRepository instancia;


    public static EntidadesBeneficiariasRepository getInstance(){
        if(instancia == null){
            instancia = new EntidadesBeneficiariasRepository();
        }
        return instancia;
    }

    public EntidadesBeneficiariasRepository(){
        this.entidades = new ArrayList<>();
    }

    public List<EntidadBeneficiaria> findAll() {
        return new ArrayList<>(entidades);
    }

    public void agregarEntidades(List<EntidadBeneficiaria> entidades){
        this.entidades.addAll(entidades);
    }

    public EntidadBeneficiaria findById(Long id) {
        return entidades.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public EntidadBeneficiaria save(EntidadBeneficiaria entidad){
        entidades.add(entidad);
        return entidad;
    }

    public void delete(Long id){
        entidades.removeIf(d -> d.getId() == id);
    }

}

/*
// Variante como bean de Spring en memoria (para que funcione la inyección por constructor)
// Nota: se conserva getInstance() porque CoordinadorAsignacion lo utiliza.
package org.donatrack.repository;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.springframework.stereotype.Repository;

@Repository
public class EntidadesBeneficiariasRepository {
    private final List<EntidadBeneficiaria> entidades = new ArrayList<>();

    public List<EntidadBeneficiaria> findAll() { return new ArrayList<>(entidades); }
    public void agregarEntidades(List<EntidadBeneficiaria> entidades){ this.entidades.addAll(entidades); }
    public EntidadBeneficiaria findById(Long id) {
        return entidades.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
    }
    public EntidadBeneficiaria save(EntidadBeneficiaria entidad){ entidades.add(entidad); return entidad; }
    public void delete(Long id){ entidades.removeIf(d -> d.getId() == id); }
}
*/
