package org.donatrack.controller.dto;

import org.donatrack.model.Entrega;
import java.util.List;

public class DepositoDTO {
    private List<Entrega> entregas;

    public DepositoDTO() {}

    public DepositoDTO(List<Entrega> entregas) {
        this.entregas = entregas;
    }

    public List<Entrega> getEntregas() { return entregas; }
    public void setEntregas(List<Entrega> entregas) { this.entregas = entregas; }
}