package org.donatrack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "camion")
public class Camion {
    @Id
    private String patente;

    private Float capacidadVolumen;
    private Float altura;
    private Float capacidadCarga;

    private Boolean activo = true;

    private Double latitud;
    private Double longitud;
    private LocalDateTime ultimaActualizacionUbicacion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ruta_activa_id")
    private RutaReparto rutaActiva;

    public Camion() {}

    public Camion(String patente, Float capacidadVolumen, Float altura, Float capacidadCarga) {
        this.patente = patente;
        this.capacidadVolumen = capacidadVolumen;
        this.altura = altura;
        this.capacidadCarga = capacidadCarga;
        this.activo = true;
    }

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }
    public Float getCapacidadVolumen() { return capacidadVolumen; }
    public void setCapacidadVolumen(Float capacidadVolumen) { this.capacidadVolumen = capacidadVolumen; }
    public Float getAltura() { return altura; }
    public void setAltura(Float altura) { this.altura = altura; }
    public Float getCapacidadCarga() { return capacidadCarga; }
    public void setCapacidadCarga(Float capacidadCarga) { this.capacidadCarga = capacidadCarga; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public LocalDateTime getUltimaActualizacionUbicacion() { return ultimaActualizacionUbicacion; }
    public void setUltimaActualizacionUbicacion(LocalDateTime ultimaActualizacionUbicacion) { this.ultimaActualizacionUbicacion = ultimaActualizacionUbicacion; }
    public RutaReparto getRutaActiva() { return rutaActiva; }
    public void setRutaActiva(RutaReparto rutaActiva) { this.rutaActiva = rutaActiva; }
}