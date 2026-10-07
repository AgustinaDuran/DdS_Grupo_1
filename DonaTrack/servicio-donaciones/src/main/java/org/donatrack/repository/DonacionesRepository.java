package org.donatrack.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.TipoEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DonacionesRepository extends JpaRepository<Donacion, Long> {
    default void agregarDonaciones(List<Donacion> donaciones) { saveAll(donaciones); }

    @Query("""
        select d from Donacion d
        where (:estado is null or d.estadoDonacion.estado = :estado)
        and (:fechaDesde is null or d.fechaIngreso >= :fechaDesde)
        and (:fechaHasta is null or d.fechaIngreso <= :fechaHasta)
        and (:subcategoria is null or d.subcategoria = :subcategoria)
        """)
    List<Donacion> buscarConFiltros(@Param("estado") TipoEstado estado,
                                    @Param("fechaDesde") LocalDateTime fechaDesde,
                                    @Param("fechaHasta") LocalDateTime fechaHasta,
                                    @Param("subcategoria") Subcategoria subcategoria);
}
