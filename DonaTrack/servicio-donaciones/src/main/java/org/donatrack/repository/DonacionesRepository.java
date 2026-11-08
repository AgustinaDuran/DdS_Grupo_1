package org.donatrack.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.TipoEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // Donaciones en un estado, paginadas y opcionalmente filtradas por subcategoría.
    // El orden lo define el Pageable que arma el service.
    @Query("""
        select d from Donacion d
        where d.estadoDonacion.estado = :estado
        and (:subcategoriaId is null or d.subcategoria.id = :subcategoriaId)
        """)
    Page<Donacion> buscarPorEstado(@Param("estado") TipoEstado estado,
                                   @Param("subcategoriaId") Long subcategoriaId,
                                   Pageable pageable);

    // Donaciones asignadas a una entidad, paginadas.
    Page<Donacion> findByEntidadAEntregarId(Long entidadId, Pageable pageable);

    // Igual que la anterior, pero solo en los estados indicados.
    Page<Donacion> findByEntidadAEntregarIdAndEstadoDonacionEstadoIn(Long entidadId,
                                                                     Collection<TipoEstado> estados,
                                                                     Pageable pageable);
}
