package org.donatrack.controller.dto;

import java.time.LocalDateTime;

import org.donatrack.model.EnumEstadoNotificacion;
import org.donatrack.model.Notificacion;

/** Resultado del intento de envio, para que el llamador sepa que paso realmente. */
public class NotificacionResultadoDTO {

    private final Long id;
    private final String destinatario;
    private final EnumEstadoNotificacion estado;
    private final LocalDateTime fechaEnvio;
    private final String motivoFallo;

    public NotificacionResultadoDTO(Notificacion notificacion) {
        this.id = notificacion.getId();
        this.destinatario = notificacion.getDestinatario();
        this.estado = notificacion.getEstado();
        this.fechaEnvio = notificacion.getFechaEnvio();
        this.motivoFallo = notificacion.getMotivoFallo();
    }

    public Long getId() { return id; }
    public String getDestinatario() { return destinatario; }
    public EnumEstadoNotificacion getEstado() { return estado; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public String getMotivoFallo() { return motivoFallo; }
}
