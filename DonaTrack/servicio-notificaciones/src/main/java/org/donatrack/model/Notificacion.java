package org.donatrack.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Notificacion {

    private static final AtomicLong CONTADOR_ID = new AtomicLong(1);

    private Long id;
    private String destinatario;
    private String nombreDestinatario;
    private String mensaje;
    private MedioEnvio medioEnvio;
    private LocalDateTime fechaEnvio;
    private EnumEstadoNotificacion estado;
    private String motivoFallo;

    public Notificacion(String destinatario, String nombreDestinatario, String mensaje, MedioEnvio medioEnvio) {
        this.id = CONTADOR_ID.getAndIncrement();
        this.destinatario = destinatario;
        this.nombreDestinatario = nombreDestinatario;
        this.mensaje = mensaje;
        this.medioEnvio = medioEnvio;
        this.estado = EnumEstadoNotificacion.PENDIENTE;
        this.fechaEnvio = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    @JsonIgnore
    public MedioEnvio getMedioEnvio() {
        return medioEnvio;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public EnumEstadoNotificacion getEstado() {
        return estado;
    }

    public String getMotivoFallo() {
        return motivoFallo;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void marcarComoEnviada() {
        this.estado = EnumEstadoNotificacion.ENVIADA;
        this.fechaEnvio = LocalDateTime.now();
        this.motivoFallo = null;
    }

    public void marcarComoFallida(String motivo) {
        this.estado = EnumEstadoNotificacion.FALLIDA;
        this.motivoFallo = motivo;
    }
}
