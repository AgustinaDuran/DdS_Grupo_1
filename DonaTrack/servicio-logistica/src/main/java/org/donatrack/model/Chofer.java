package org.donatrack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "choferes")
public class Chofer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "camion_patente", referencedColumnName = "patente")
    private Camion camion;

    public Chofer() {}

    public Chofer(Camion camion) {
        this.camion = camion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Camion getCamion() { return camion; }
    public void setCamion(Camion camion) { this.camion = camion; }
}