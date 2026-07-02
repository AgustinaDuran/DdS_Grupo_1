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
        usuarios.add(donante);
        return donante;
    }

    public void delete(Long id){
        usuarios.removeIf(d -> d.getId() == id);
    }
}
