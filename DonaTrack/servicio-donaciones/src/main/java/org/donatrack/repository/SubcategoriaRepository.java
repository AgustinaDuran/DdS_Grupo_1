package org.donatrack.repository;

import java.util.List;
import org.donatrack.dominio.categoria.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {
    @Query("""
        select s from Subcategoria s
        where (:categoriaId is null or s.categoria.id = :categoriaId)
        and (:nombre is null or lower(s.nombre) like lower(concat('%', :nombre, '%')))
        """)
    List<Subcategoria> buscarSubcategorias(@Param("categoriaId") Long categoriaId,
                                           @Param("nombre") String nombre);
}
