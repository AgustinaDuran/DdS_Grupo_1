package org.donatrack.repository;

import java.util.List;
import org.donatrack.dominio.necesidades.Necesidad;
import org.donatrack.dominio.necesidades.TipoNecesidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NecesidadesRepository extends JpaRepository<Necesidad, Long> {
    default void agregarNecesidades(List<Necesidad> necesidades) { saveAll(necesidades); }

    default List<Necesidad> buscarConFiltros(Long entidadId, Long subcategoriaId,
                                             TipoNecesidad tipo, Boolean activa) {
        if (tipo == null) {
            return buscarSinTipo(entidadId, subcategoriaId, activa);
        }
        if (tipo == TipoNecesidad.RECURRENTE) {
            return buscarRecurrentes(entidadId, subcategoriaId, activa);
        }
        return buscarExtraordinarias(entidadId, subcategoriaId, activa);
    }

    @Query("""
        select n from Necesidad n
        where (:entidadId is null or n.entidad.id = :entidadId)
        and (:subcategoriaId is null or n.subcategoria.id = :subcategoriaId)
        and (:activa is null or n.activa = :activa)
        """)
    List<Necesidad> buscarSinTipo(@Param("entidadId") Long entidadId,
                                  @Param("subcategoriaId") Long subcategoriaId,
                                  @Param("activa") Boolean activa);

    @Query("""
        select n from Necesidad n
        where type(n) = NecesidadRecurrente
        and (:entidadId is null or n.entidad.id = :entidadId)
        and (:subcategoriaId is null or n.subcategoria.id = :subcategoriaId)
        and (:activa is null or n.activa = :activa)
        """)
    List<Necesidad> buscarRecurrentes(@Param("entidadId") Long entidadId,
                                      @Param("subcategoriaId") Long subcategoriaId,
                                      @Param("activa") Boolean activa);

    @Query("""
        select n from Necesidad n
        where type(n) = NecesidadExtraordinaria
        and (:entidadId is null or n.entidad.id = :entidadId)
        and (:subcategoriaId is null or n.subcategoria.id = :subcategoriaId)
        and (:activa is null or n.activa = :activa)
        """)
    List<Necesidad> buscarExtraordinarias(@Param("entidadId") Long entidadId,
                                          @Param("subcategoriaId") Long subcategoriaId,
                                          @Param("activa") Boolean activa);
}
