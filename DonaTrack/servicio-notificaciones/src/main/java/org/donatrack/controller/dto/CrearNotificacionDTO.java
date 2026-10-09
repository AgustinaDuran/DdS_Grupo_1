package org.donatrack.controller.dto;

import java.util.List;

import org.donatrack.model.Contacto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class CrearNotificacionDTO {

    private Long destinatarioId;
    private String nombreDestinatario;

    @NotEmpty(message = "Se debe informar al menos un contacto")
    private List<Contacto> contactos;

    @NotBlank(message = "El mensaje no puede estar vacio")
    private String mensaje;

    public CrearNotificacionDTO() {
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setDestinatarioId(Long destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }
}
