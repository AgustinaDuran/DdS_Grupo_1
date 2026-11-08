package org.donatrack.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.donatrack.dominio.algoritmo.ResultadoAsignacion;
import org.donatrack.dominio.donacion.TipoEstado;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultadoAsignacionRepository extends JpaRepository<ResultadoAsignacion, Long> {

    // @EntityGraph trae los ítems y sus entidades en la misma consulta (evita una consulta extra por resultado).
    @EntityGraph(attributePaths = {"items", "items.entidad"})
    Optional<ResultadoAsignacion> findByDonacionId(Long donacionId);

    // Para el listado paginado: trae los rankings de toda la página en una sola consulta.
    @EntityGraph(attributePaths = {"items", "items.entidad"})
    List<ResultadoAsignacion> findByDonacionIdIn(Collection<Long> donacionIds);

    // Spring Data carga y borra cada resultado, así el cascade también borra sus ítems.
    // Necesita una transacción activa (@Transactional en el service que lo llame).
    void deleteByDonacionId(Long donacionId);

    // Rankings de donaciones que ya salieron de ese estado (para limpiarlos en el job).
    @Query("select r from ResultadoAsignacion r where r.donacion.estadoDonacion.estado <> :estado")
    List<ResultadoAsignacion> findByDonacionFueraDeEstado(@Param("estado") TipoEstado estado);
}
