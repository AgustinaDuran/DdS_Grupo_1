package org.donatrack.model;

import java.time.LocalDateTime;

public class Notificacion {
    private long id;
    private String destinatario;
    private String mensaje;
    private MedioEnvio medioEnvio;
    private LocalDateTime fechaEnvio;
    private boolean estado;

    public Notificacion(String destinatario, String mensaje, MedioEnvio medio) {
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.medioEnvio = medio;
        this.estado = false;
        this.fechaEnvio = LocalDateTime.now();
    }

    public long getId() {
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

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void enviarNotificacion() {
        this.estado = true;
        this.medioEnvio.notificar(this.destinatario, this.mensaje);
    }
}
