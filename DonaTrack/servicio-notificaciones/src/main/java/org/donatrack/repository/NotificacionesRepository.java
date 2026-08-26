package org.donatrack.repository;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

import org.donatrack.model.Notificacion;
import org.springframework.stereotype.Repository;

/**
 * Historial de notificaciones en memoria. Se pierde al reiniciar el servicio.
 */
@Repository
public class NotificacionesRepository {

    private final List<Notificacion> notificaciones = new CopyOnWriteArrayList<>();

    public List<Notificacion> findAll() {
        return List.copyOf(notificaciones);
    }

    public Notificacion findById(Long id) {
        return notificaciones.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(Notificacion notificacion) {
        notificaciones.add(notificacion);
    }

    public void delete(Long id) {
        notificaciones.removeIf(n -> n.getId().equals(id));
    }
}
