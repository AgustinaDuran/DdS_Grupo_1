package org.donatrack.repository;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.dominio.necesidades.Necesidad;
import org.donatrack.dominio.necesidades.TipoNecesidad;

public class NecesidadesRepository { // singleton

    private List<Necesidad> necesidades;

    private NecesidadesRepository() { // constructor privado
        necesidades = new ArrayList<>();
    }

    public void agregarNecesidades(List<Necesidad> necesidades) {
        this.necesidades.addAll(necesidades);
    }

    public List<Necesidad> findAll() {
        return necesidades;
    }

    public Necesidad findById(Long id) {
        return necesidades.stream()
                .filter(n -> n.getId() != null && n.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Necesidad> buscarConFiltros(Long entidadId, Long subcategoriaId, TipoNecesidad tipo, Boolean activa) {
        return necesidades;
    }

    public void save(Necesidad necesidad) {
        necesidades.add(necesidad);
    }

    public void saveAll(List<Necesidad> necesidades) {
        this.necesidades.addAll(necesidades);
    }

    public void delete(Long id) {
        necesidades = necesidades.stream()
                .filter(n -> n.getId() == null || n.getId() != id)
                .toList();
    }

}

/*
// Variante como bean de Spring en memoria (para que funcione la inyección por constructor)
package org.donatrack.repository;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.dominio.necesidades.Necesidad;
import org.donatrack.dominio.necesidades.TipoNecesidad;
import org.springframework.stereotype.Repository;

@Repository
public class NecesidadesRepository {

    private List<Necesidad> necesidades;

    public NecesidadesRepository() {
        necesidades = new ArrayList<>();
    }

    public void agregarNecesidades(List<Necesidad> necesidades) {
        this.necesidades.addAll(necesidades);
    }

    public List<Necesidad> findAll() {
        return necesidades;
    }

    public Necesidad findById(Long id) {
        return necesidades.stream()
                .filter(n -> n.getId() != null && n.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Necesidad> buscarConFiltros(Long entidadId, Long subcategoriaId, TipoNecesidad tipo, Boolean activa) {
        return necesidades;
    }

    public void save(Necesidad necesidad) {
        necesidades.add(necesidad);
    }

    public void saveAll(List<Necesidad> necesidades) {
        this.necesidades.addAll(necesidades);
    }

    public void delete(Long id) {
        necesidades = necesidades.stream()
                .filter(n -> n.getId() == null || n.getId() != id)
                .toList();
    }
}
*/
