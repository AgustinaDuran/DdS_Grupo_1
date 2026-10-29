package org.donatrack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "entregas")
public class Entrega {
    @Id
    private String id;                       // ObjectId generado por Mongo
    
    @Indexed
    private String donacionId;       
    private String direccionDestino;  
    
    @Indexed
    private EstadoEntrega estado = EstadoEntrega.PENDIENTE;

    
    private Integer ordenVisita;
    private String fotoComprobanteUrl;
    private LocalDateTime fechaHoraEntrega; 
    private String motivoNoRecibida;
    private String patenteCamion;
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private List<CambioEstado> historial = new ArrayList<>();   // subdocumentos embebidos


    public Entrega() {}

    public Entrega(String donacionId, String direccionDestino) {
        this.donacionId = donacionId;
        this.direccionDestino = direccionDestino;
        this.estado = EstadoEntrega.PENDIENTE;
        this.historial.add(new CambioEstado(EstadoEntrega.PENDIENTE, null));
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
        this.historial.add(new CambioEstado(nuevoEstado, motivo));

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
    public void asignarCamion(String patenteCamion) {
        this.patenteCamion = patenteCamion;
    }

    public Boolean EsEstadoTerminal() {
        return this.estado == EstadoEntrega.ENTREGADA || this.estado == EstadoEntrega.NO_RECIBIDA;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public String getPatenteCamion() { return patenteCamion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public List<CambioEstado> getHistorial() { return historial; }

}