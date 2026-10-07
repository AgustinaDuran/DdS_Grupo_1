package org.donatrack.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "notificaciones")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destinatario;
    private String nombreDestinatario;
    private String mensaje;
    @Transient
    private MedioEnvio medioEnvio;
    private String tipoMedio;
    private LocalDateTime fechaEnvio;
    @Enumerated(EnumType.STRING)
    private EnumEstadoNotificacion estado;
    private String motivoFallo;

    protected Notificacion() {
    }

    public Notificacion(String destinatario, String nombreDestinatario, String mensaje, MedioEnvio medioEnvio) {
        this.destinatario = destinatario;
        this.nombreDestinatario = nombreDestinatario;
        this.mensaje = mensaje;
        this.medioEnvio = medioEnvio;
        this.tipoMedio = medioEnvio == null ? null : medioEnvio.getClass().getSimpleName();
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
