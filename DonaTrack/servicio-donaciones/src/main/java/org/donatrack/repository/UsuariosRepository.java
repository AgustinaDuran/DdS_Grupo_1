package org.donatrack.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.springframework.stereotype.Repository;

@Repository
public class UsuariosRepository {
    private final List<DatosUsuario> usuarios = new ArrayList<>();
    private Long nextId = 1L;

    public List<DatosUsuario> findAll() {
        return new ArrayList<>(usuarios);
    }

    public void agregarDatosUsuarios(List<DatosUsuario> usuarios){
        this.usuarios.addAll(usuarios);
    }

    public Optional<DatosUsuario> findById(Long id) {
        return usuarios.stream()
                .filter(d -> d.getId() == id)
                .findFirst();
    }

    public DatosUsuario save(DatosUsuario donante){
        if (donante.getId() == null) {
            donante.setId(nextId++);
        } else {
            usuarios.removeIf(existente -> donante.getId().equals(existente.getId()));
        }
        usuarios.add(donante);
        return donante;
    }

    public void delete(Long id){
        usuarios.removeIf(d -> d.getId() == id);
    }
}
