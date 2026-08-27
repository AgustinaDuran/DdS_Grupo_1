package org.donatrack.integracion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

/**
 * Proyección de una Entrega devuelta por el Servicio de Logística
 * (GET /api/logistica/entregas). Sólo se mapean los campos que consume el polling.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntregaResponse {
    private Long id;
    private String donacionId;
    private String direccionDestino;
    private String estado;
    private Integer ordenVisita;
    private String fotoComprobanteUrl;
    private LocalDateTime fechaHoraEntrega;
    private String motivoNoRecibida;
    private String patenteCamion;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDonacionId() {
        return donacionId;
    }

    public void setDonacionId(String donacionId) {
        this.donacionId = donacionId;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getOrdenVisita() {
        return ordenVisita;
    }

    public void setOrdenVisita(Integer ordenVisita) {
        this.ordenVisita = ordenVisita;
    }

    public String getFotoComprobanteUrl() {
        return fotoComprobanteUrl;
    }

    public void setFotoComprobanteUrl(String fotoComprobanteUrl) {
        this.fotoComprobanteUrl = fotoComprobanteUrl;
    }

    public LocalDateTime getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public void setFechaHoraEntrega(LocalDateTime fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public String getMotivoNoRecibida() {
        return motivoNoRecibida;
    }

    public void setMotivoNoRecibida(String motivoNoRecibida) {
        this.motivoNoRecibida = motivoNoRecibida;
    }

    public String getPatenteCamion(){
        return this.patenteCamion;
    }

    public void setPatenteCamion(String patente){
        this.patenteCamion = patente;
    }
}
