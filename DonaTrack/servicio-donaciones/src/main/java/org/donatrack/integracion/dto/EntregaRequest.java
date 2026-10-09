package org.donatrack.integracion.dto;

/**
 * Una entrega a registrar en el Servicio de Logística.
 * Se corresponde con el modelo Entrega { donacionId, direccionDestino } de logística.
 */
public class EntregaRequest {
    private String donacionId;
    private String direccionDestino;

    public EntregaRequest() {
    }

    public EntregaRequest(String donacionId, String direccionDestino) {
        this.donacionId = donacionId;
        this.direccionDestino = direccionDestino;
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
}
