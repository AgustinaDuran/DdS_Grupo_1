package org.donatrack.controller.dto;

public class ActualizarRutaDTO {
    public static final String EN_CURSO = "EN_CURSO";

    private String estado;

    public ActualizarRutaDTO() {}

    public ActualizarRutaDTO(String estado) {
        this.estado = estado;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
