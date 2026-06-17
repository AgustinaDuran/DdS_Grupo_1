package org.donatrack.repository;
import org.donatrack.dominio.donacion.Donacion;

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
    public void save(Donacion donacion){
        donaciones.add(donacion);
    }
    public void delete(long id){
        donaciones = donaciones.stream().filter(d -> d.getId() != id).toList();
    }
    

}

/* 
//para cuando usemos base de datos
public interface DonacionesRepository extends JpaRepository<Donaciones, long> {

    //Ejemplo de query: cantidad de usuarios con mas donaciones
    @Query("SELECT COUNT(d) FROM DonanteIncentivos d WHERE SIZE(d.donaciones) > " + "(SELECT SIZE(du.donaciones) FROM DonanteIncentivos du WHERE du.nombreUsuario = :nombreUsuario)")
    long countDonantesConMasDonaciones(@Param("nombreUsuario") String nombreUsuario);
}


 */
