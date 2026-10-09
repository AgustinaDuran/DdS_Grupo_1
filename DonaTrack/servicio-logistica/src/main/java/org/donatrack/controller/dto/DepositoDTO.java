package org.donatrack.controller.dto;

import java.util.List;

public class DepositoDTO {
    private List<EntregaEntranteDTO> entregas;

    public DepositoDTO() {}

    public DepositoDTO(List<EntregaEntranteDTO> entregas) {
        this.entregas = entregas;
    }

    public List<EntregaEntranteDTO> getEntregas() { return entregas; }
    public void setEntregas(List<EntregaEntranteDTO> entregas) { this.entregas = entregas; }
}