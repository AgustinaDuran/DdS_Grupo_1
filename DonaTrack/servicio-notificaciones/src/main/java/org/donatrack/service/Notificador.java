package org.donatrack.service;

import org.donatrack.model.*;
import org.donatrack.repository.NotificacionesRepository;
import org.springframework.stereotype.Service;
import org.donatrack.controller.dto.*;

import java.util.List;



@Service
public class Notificador {

    private NotificacionesRepository notificacionesRepository;

    public void notificar(CrearNotificacionDTO crearNotificacionDTO) {

        List<Contacto> contactos = crearNotificacionDTO.getContactos();

        Contacto contactoSeleccionado = contactos.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontraron contactos"));

        MedioEnvio medio = obtenerMedioDesdeTipo(contactoSeleccionado.getTipo(), contactoSeleccionado.getValor());

        Notificacion notificacion = new Notificacion(
                contactoSeleccionado.getValor(),
                crearNotificacionDTO.getMensaje(),
                medio
        );

        try {
            medio.notificar(notificacion.getDestinatario(), notificacion.getMensaje());
            notificacion.marcarComoEnviada();
        } catch (Exception e) {
            notificacion.marcarComoFallida();
        }

        this.notificacionesRepository.save(notificacion);

    }

    private MedioEnvio obtenerMedioDesdeTipo(String tipo, String valor) {

        return switch (tipo.toUpperCase()) {
            case "MAIL" -> new EnvioMail(valor);
            case "WHATSAPP" -> new EnvioWhatsapp(valor);
            case "SMS" -> new EnvioSMS(valor);
            default -> throw new RuntimeException("Medio de envío no soportado: " + tipo);
        };
    }

    public List<Notificacion> obtenerNotificaciones() {
        return this.notificacionesRepository.findAll();
    }
}


