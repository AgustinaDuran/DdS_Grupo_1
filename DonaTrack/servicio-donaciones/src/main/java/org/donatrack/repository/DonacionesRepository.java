package org.donatrack.repository;
import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.categoria.*;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Repository
public class DonacionesRepository {

    private List<Donacion> donaciones; // a reemplazar por un
    private Long nextId = 1L;


    public DonacionesRepository() {
        donaciones = new ArrayList<>();
    }

    
    public void agregarDonaciones(List<Donacion> donaciones){
        this.donaciones.addAll(donaciones);
    }

    public List<Donacion> findAll(){
        return donaciones;
    }

    public Donacion findById(Long id){
        return donaciones.stream()
                .filter(d -> d.getId() != null && d.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    public List<Donacion> buscarConFiltros(TipoEstado tipoEstado, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Subcategoria subcategoria){
        return donaciones;

    };


    public void save(Donacion donacion){
        if (donacion.getId() == null) {
            donacion.setId(nextId++);
        } else {
            donaciones.removeIf(existente -> donacion.getId().equals(existente.getId()));
        }
        donaciones.add(donacion);
    }

    public void saveAll(List<Donacion> donaciones){
        for (Donacion donacion : donaciones) {
            save(donacion);
        }
    }

    public void delete(Long id){
        donaciones = donaciones.stream().filter(d -> d.getId() != id).collect(Collectors.toCollection(ArrayList::new));
    }
    

}

/* 
//para cuando usemos base de datos
public interface DonacionesRepository extends JpaRepository<Donaciones, Long> {

    //Ejemplo de query: busqueda de donacion filtrada
    @Query("""
    SELECT d
    FROM Donacion d
    WHERE (:estado IS NULL OR d.estadoDonacion.estado = :estado)
    AND (:fechaDesde IS NULL OR d.fechaIngreso >= :fechaDesde)
    AND (:fechaHasta IS NULL OR d.fechaIngreso <= :fechaHasta)
    AND (:subcategoria IS NULL OR d.subcategoria = :subcategoria)
    """)
    List<Donacion> buscarConFiltros(
        @Param("estado") TipoEstado estado,
        @Param("fechaDesde") LocalDateTime fechaDesde,
        @Param("fechaHasta") LocalDateTime fechaHasta,
        @Param("subcategoria") Subcategoria subcategoria
);
}


 */
