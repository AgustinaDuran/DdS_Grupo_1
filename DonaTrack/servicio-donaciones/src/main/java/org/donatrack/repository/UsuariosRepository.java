package org.donatrack.repository;

import java.util.List;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosRepository extends JpaRepository<DatosUsuario, Long> {
    default void agregarDatosUsuarios(List<DatosUsuario> usuarios) { saveAll(usuarios); }
}
