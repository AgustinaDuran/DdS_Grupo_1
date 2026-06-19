package org.donatrack.service;

import org.springframework.stereotype.Service;

import org.donatrack.controller.dto.CrearNotificacionDTO;
import org.donatrack.model.MedioEnvio;
import org.donatrack.model.Notificacion;
import org.donatrack.model.contacto.Contacto;
import org.donatrack.repository.NotificacionesRepository;

import java.util.List;

@Service
public class Notificador {

    private final NotificacionesRepository notificacionesRepository;

    public Notificador() {
        this(NotificacionesRepository.getInstance());
    }

    public Notificador(NotificacionesRepository notificacionesRepository) {
        this.notificacionesRepository = notificacionesRepository;
    }

    public void notificar(CrearNotificacionDTO crearNotificacionDTO) {
        List<Contacto> contactos = crearNotificacionDTO.getContactos();

        // Toma el primer contacto; acá iría la lógica de elegir contacto si hubiera.
        Contacto contactoSeleccionado = contactos.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontraron contactos"));

        MedioEnvio medio = contactoSeleccionado.pasarAMedioEnvio();

        Notificacion notificacion = new Notificacion(
                crearNotificacionDTO.getnombreDestinatario(),
                crearNotificacionDTO.getMensaje(),
                medio);

        // Se envía inmediatamente; posible reforma a un job en próximas iteraciones.
        notificacion.enviarNotificacion();

        this.notificacionesRepository.save(notificacion);
    }
}
