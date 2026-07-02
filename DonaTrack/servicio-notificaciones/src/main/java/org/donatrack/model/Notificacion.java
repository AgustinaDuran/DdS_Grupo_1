package org.donatrack.model;

import java.time.LocalDateTime;
import org.donatrack.model.MedioEnvio;


public class Notificacion {

    private Long id;
    private String destinatario;
    private String mensaje;
    private MedioEnvio medioEnvio;
    private LocalDateTime fechaEnvio;
    private EnumEstadoNotificacion estado;

    public Long getId() {
        return id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public MedioEnvio getMedioEnvio() {
        return medioEnvio;
    }

    public EnumEstadoNotificacion getEstado() {
        return estado;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Notificacion(String destinatario, String mensaje, MedioEnvio medioEnvio) {
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.medioEnvio = medioEnvio;
        this.estado = EnumEstadoNotificacion.PENDIENTE;
        this.fechaEnvio = LocalDateTime.now();
    }

    public void marcarComoEnviada() {
        this.estado = EnumEstadoNotificacion.ENVIADA;
        this.fechaEnvio = LocalDateTime.now();
    }

    public void marcarComoFallida() {
        this.estado = EnumEstadoNotificacion.FALLIDA;
    }
}
