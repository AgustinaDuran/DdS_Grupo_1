package org.donatrack.repository;

import org.donatrack.model.notificacion;

public class NotificacionesRepository { // singleton

    private List<Notificacion> notificaciones;
    private static NotificacionesRepository instancia;

    private NotificacionesRepository() {
        notificaciones = new ArrayList<>();
    }

    public static NotificacionesRepository getInstance(){
        if(instancia == null){
            instancia = new NotificacionesRepository();
        }
        return instancia
    }


    public List<Notificacion> findAll(){
        return notificaciones;
    }

    public Notificacion findById(long id){
        return notificaciones.stream()
                .filter(n -> n.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Notificacion notificacion){
        notificaciones.add(notificacion);
    }

    public void delete(long id){
        notificaciones = notificaciones.stream().filter(n -> n.getId() != id);
    }
    

}



/* 
//para cuando usemos base de datos
public interface NotificacionesRepository extends JpaRepository<Notificaciones, long> {

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