package org.donatrack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "choferes")
public class Chofer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "camion_id", referencedColumnName = "id")
    private Camion camion;

    public Chofer() {}

    public Chofer(String nombre, String apellido, String documento, Camion camion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.camion = camion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public Camion getCamion() { return camion; }
    public void setCamion(Camion camion) { this.camion = camion; }
}