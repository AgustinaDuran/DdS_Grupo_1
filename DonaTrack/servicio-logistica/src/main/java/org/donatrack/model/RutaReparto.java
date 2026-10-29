package org.donatrack.model;

import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class RutaReparto {
    private String id = UUID.randomUUID().toString();
    private String patenteCamion;
    private LocalDate fechaPlanificacion = LocalDate.now();
    private LocalDateTime fechaHoraInicio;

                              // vive embebida dentro del camion
     @DocumentReference      // guarda solo los ids, resuelve las entregas al leer 
                            
    private List<Entrega> entregas = new ArrayList<>();

    public RutaReparto() {}

    public RutaReparto(String patenteCamion) { this.patenteCamion = patenteCamion; }

    public Boolean EstaFinalizada() {
        for (Entrega e : entregas) {
            if (!e.EsEstadoTerminal()) {
                return false;
            }
        }
        return !entregas.isEmpty();
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatenteCamion() { return patenteCamion; }
    public void setPatenteCamion(String patenteCamion) { this.patenteCamion = patenteCamion; }
    public LocalDate getFechaPlanificacion() { return fechaPlanificacion; }
    public void setFechaPlanificacion(LocalDate fechaPlanificacion) { this.fechaPlanificacion = fechaPlanificacion; }
    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }
    public List<Entrega> getEntregas() { return entregas; }
    public void setEntregas(List<Entrega> entregas) { this.entregas = entregas; }
}