package org.donatrack.controller.dto;

public class EntregaEntranteDTO {
    private String donacionId;
    private String direccionDestino;

    public EntregaEntranteDTO() {}

    public String getDonacionId() { return donacionId; }
    public void setDonacionId(String donacionId) { this.donacionId = donacionId; }
    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }
}