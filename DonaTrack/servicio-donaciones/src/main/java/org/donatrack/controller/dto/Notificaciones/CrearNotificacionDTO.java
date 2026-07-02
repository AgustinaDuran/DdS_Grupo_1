package org.donatrack.controller.dto.Notificaciones;

import java.util.List;
import org.donatrack.dominio.contacto.Contacto;


public class CrearNotificacionDTO{
    private Long destinatarioId;
    private String nombreDestinatario;
    private List<Contacto> contactos;
    private String mensaje;

    public CrearNotificacionDTO() { 
    }

    public Long getDestinatarioId(){
        return destinatarioId;
    }
    public String getMensaje() {
        return mensaje;
    }
    public List<Contacto> getContactos(){
        return contactos;
    }
    public String getnombreDestinatario(){
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