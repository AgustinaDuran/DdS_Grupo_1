package org.donatrack.controller.dto;

public class MisionProgresoDTO {
    public String descripcion; 
    public Integer progresoActual;
    public Integer objetivo;
    public Integer distanciaRestante;
    public String nombreInsigniaPremio;

    public MisionProgresoDTO(String desc, Integer prog, Integer obj, Integer dist, String ins) {
        this.descripcion = desc;
        this.progresoActual = prog;
        this.objective = obj;
        this.distanciaRestante = dist;
        this.nombreInsigniaPremio = ins;
    }
}