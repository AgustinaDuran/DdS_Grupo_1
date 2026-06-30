package org.donatrack.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ruta_reparto")
public class RutaReparto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patenteCamion;

    private LocalDate fechaPlanificacion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ruta_id")
    private List<Entrega> entregas = new ArrayList<>();

    public RutaReparto() {
        this.fechaPlanificacion = LocalDate.now();
    }

    public RutaReparto(String patenteCamion) {
        this();
        this.patenteCamion = patenteCamion;
    }

    public Boolean EstaFinalizada() {
        for (Entrega e : entregas) {
            if (!e.EsEstadoTerminal()) {
                return false;
            }
        }
        return !entregas.isEmpty();
    }

    public String getPatenteCamion() { return patenteCamion; }
    public void setPatenteCamion(String patenteCamion) { this.patenteCamion = patenteCamion; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFechaPlanificacion() { return fechaPlanificacion; }
    public void setFechaPlanificacion(LocalDate fechaPlanificacion) { this.fechaPlanificacion = fechaPlanificacion; }
    public List<Entrega> getEntregas() { return entregas; }
    public void setEntregas(List<Entrega> entregas) { this.entregas = entregas; }
}