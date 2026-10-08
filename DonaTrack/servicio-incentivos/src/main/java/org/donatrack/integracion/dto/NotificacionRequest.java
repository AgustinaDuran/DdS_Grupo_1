package org.donatrack.integracion.dto;

import java.util.List;

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

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<ContactoNotificacion> getContactos() {
        return contactos;
    }
}
