package org.donatrack.repository;
import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.categoria.*;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



public class DonacionesRepository { // singleton

    private List<Donacion> donaciones; // a reemplazar por un


    private DonacionesRepository() { //constructor privado
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
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
    }


    public List<Donacion> buscarConFiltros(TipoEstado tipoEstado, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Subcategoria subcategoria){
        return donaciones;

    };


    public void save(Donacion donacion){
        donaciones.add(donacion);
    }

    public void saveAll(List<Donacion> donaciones){
        this.donaciones.addAll(donaciones);
    }

    public void delete(Long id){
        donaciones = donaciones.stream().filter(d -> d.getId() != id).toList();
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
