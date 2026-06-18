package org.donatrack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "puestos_ranking")
public class PuestoRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer posicion;
    private String nombreUsuario;
    private Integer misionesCumplidasEnElMes;

    public PuestoRanking() {}

    public PuestoRanking(Integer posicion, String nombreUsuario, Integer misionesCumplidasEnElMes) {
        this.posicion = posicion;
        this.nombreUsuario = nombreUsuario;
        this.misionesCumplidasEnElMes = misionesCumplidasEnElMes;
    }

    public Integer getPosicion() { return posicion; }
    public String getNombreUsuario() { return nombreUsuario; }
    public Integer getMisionesCumplidasEnElMes() { return misionesCumplidasEnElMes; }
}