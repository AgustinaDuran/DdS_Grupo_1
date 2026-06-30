package org.donatrack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "camion")
public class Camion {
    @Id
    private String patente;

    private Float capacidadVolumen;
    private Float altura;
    private Float capacidadCarga;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ruta_activa_id")
    private RutaReparto rutaActiva;

    public Camion() {}

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }
    public Float getCapacidadVolumen() { return capacidadVolumen; }
    public void setCapacidadVolumen(Float capacidadVolumen) { this.capacidadVolumen = capacidadVolumen; }
    public Float getAltura() { return altura; }
    public void setAltura(Float altura) { this.altura = altura; }
    public Float getCapacidadCarga() { return capacidadCarga; }
    public void setCapacidadCarga(Float capacidadCarga) { this.capacidadCarga = capacidadCarga; }
    public RutaReparto getRutaActiva() { return rutaActiva; }
    public void setRutaActiva(RutaReparto rutaActiva) { this.rutaActiva = rutaActiva; }
}