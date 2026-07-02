package org.donatrack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "entrega")
public class Entrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String donacionId;       
    private String direccionDestino;  
    
    @Enumerated(EnumType.STRING)
    private EstadoEntrega estado;
    
    private Integer ordenVisita;
    private String fotoComprobanteUrl;
    private LocalDateTime fechaHoraEntrega; 
    private String motivoNoRecibida;

    public Entrega() {}

    public Entrega(String donacionId, String direccionDestino) {
        this.donacionId = donacionId;
        this.direccionDestino = direccionDestino;
        this.estado = EstadoEntrega.PENDIENTE;
    }

    public void CambiarEstado(EstadoEntrega nuevoEstado, String fotoUrl, String motivo) {
        boolean transicionValida = switch (this.estado) {
            case PENDIENTE -> nuevoEstado == EstadoEntrega.EN_TRASLADO;
            case EN_TRASLADO -> nuevoEstado == EstadoEntrega.ENTREGADA || nuevoEstado == EstadoEntrega.NO_RECIBIDA;
            case NO_RECIBIDA -> nuevoEstado == EstadoEntrega.PENDIENTE;
            case ENTREGADA -> false; // estado terminal
        };

        if (!transicionValida) {
            throw new IllegalStateException("Transición inválida: " + this.estado + " -> " + nuevoEstado);
        }

        this.estado = nuevoEstado;

        if (nuevoEstado == EstadoEntrega.ENTREGADA) {
            this.fechaHoraEntrega = LocalDateTime.now();
            this.fotoComprobanteUrl = fotoUrl;
        }

        if (nuevoEstado == EstadoEntrega.NO_RECIBIDA) {
            this.motivoNoRecibida = motivo;
        }

        if (nuevoEstado == EstadoEntrega.PENDIENTE) {
            this.ordenVisita = null; //vuelve al depostito
        }
    }

    public Boolean EsEstadoTerminal() {
        return this.estado == EstadoEntrega.ENTREGADA || this.estado == EstadoEntrega.NO_RECIBIDA;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDonacionId() { return donacionId; }
    public void setDonacionId(String donacionId) { this.donacionId = donacionId; }
    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }
    public EstadoEntrega getEstado() { return estado; }
    public Integer getOrdenVisita() { return ordenVisita; }
    public void setOrdenVisita(Integer ordenVisita) { this.ordenVisita = ordenVisita; }
    public String getFotoComprobanteUrl() { return fotoComprobanteUrl; }
    public LocalDateTime getFechaHoraEntrega() { return fechaHoraEntrega; }
    public String getMotivoNoRecibida() { return motivoNoRecibida; }
}