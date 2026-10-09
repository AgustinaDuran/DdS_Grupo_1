package org.donatrack.repository;

import java.util.List;
import org.donatrack.dominio.categoria.Categoria;
import org.donatrack.dominio.categoria.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriasRepository extends JpaRepository<Categoria, Long> {
    default List<Categoria> findAllCategorias() { return findAll(); }
    default void saveCategoria(Categoria categoria) { save(categoria); }
    default void deleteCategoria(Long id) { deleteById(id); }
}
