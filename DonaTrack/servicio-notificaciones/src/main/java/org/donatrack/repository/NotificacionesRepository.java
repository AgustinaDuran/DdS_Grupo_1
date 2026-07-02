package org.donatrack.repository;

import org.donatrack.model.Notificacion;

import java.util.ArrayList;
import java.util.List;

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
        return instancia;
    }


    public List<Notificacion> findAll(){
        return notificaciones;
    }

    public Notificacion findById(Long id){
        return notificaciones.stream()
                .filter(n -> n.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Notificacion notificacion){
        notificaciones.add(notificacion);
    }

    public void delete(Long id){
        notificaciones = notificaciones.stream()
                .filter(n -> !n.getId().equals(id))
                .toList();
    }


}
