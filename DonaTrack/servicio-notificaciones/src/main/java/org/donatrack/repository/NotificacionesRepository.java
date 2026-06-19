package org.donatrack.repository;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.model.Notificacion;

public class NotificacionesRepository { // singleton

    private List<Notificacion> notificaciones;
    private static NotificacionesRepository instancia;

    public NotificacionesRepository() {
        notificaciones = new ArrayList<>();
    }

    public static NotificacionesRepository getInstance() {
        if (instancia == null) {
            instancia = new NotificacionesRepository();
        }
        return instancia;
    }

    public List<Notificacion> findAll() {
        return notificaciones;
    }

    public Notificacion findById(long id) {
        return notificaciones.stream()
                .filter(n -> n.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Notificacion notificacion) {
        notificaciones.add(notificacion);
    }

    public void delete(long id) {
        notificaciones.removeIf(n -> n.getId() == id);
    }
}
