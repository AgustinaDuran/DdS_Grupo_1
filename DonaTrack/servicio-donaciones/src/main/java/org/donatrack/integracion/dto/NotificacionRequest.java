package org.donatrack.integracion.dto;

import java.util.List;

/**
 * Payload que consume el Servicio de Notificaciones en POST /api/notificaciones.
 */
public class NotificacionRequest {
    private Long destinatarioId;
    private String nombreDestinatario;
    private String mensaje;
    private List<ContactoNotificacion> contactos;

    public NotificacionRequest() {
    }

    public NotificacionRequest(Long destinatarioId, String nombreDestinatario, String mensaje,
                               List<ContactoNotificacion> contactos) {
        this.destinatarioId = destinatarioId;
        this.nombreDestinatario = nombreDestinatario;
        this.mensaje = mensaje;
        this.contactos = contactos;
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(Long destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<ContactoNotificacion> getContactos() {
        return contactos;
    }

    public void setContactos(List<ContactoNotificacion> contactos) {
        this.contactos = contactos;
    }
}
