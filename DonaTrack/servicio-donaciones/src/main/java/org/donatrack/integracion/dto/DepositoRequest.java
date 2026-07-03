package org.donatrack.integracion.dto;

import java.util.List;

/**
 * Payload que consume el Servicio de Logística en POST /api/logistica/entregas
 * (se corresponde con su DepositoDTO { entregas }).
 */
public class DepositoRequest {
    private List<EntregaRequest> entregas;

    public DepositoRequest() {
    }

    public DepositoRequest(List<EntregaRequest> entregas) {
        this.entregas = entregas;
    }

    public List<EntregaRequest> getEntregas() {
        return entregas;
    }

    public void setEntregas(List<EntregaRequest> entregas) {
        this.entregas = entregas;
    }
}
