package org.donatrack.controller.dto;

import org.donatrack.model.EstadoEntrega;

public class ActualizarEntregaDTO {
    private EstadoEntrega estado;
    private String fotoUrl;
    private String motivo;

    public ActualizarEntregaDTO() {}

    public ActualizarEntregaDTO(EstadoEntrega estado, String fotoUrl, String motivo) {
        this.estado = estado;
        this.fotoUrl = fotoUrl;
        this.motivo = motivo;
    }

    public EstadoEntrega getEstado() { return estado; }
    public void setEstado(EstadoEntrega estado) { this.estado = estado; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
