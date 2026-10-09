package org.donatrack.controller.dto;
 
public class CamionDTO {
    private String patente;
    private Float capacidadVolumen;
    private Float altura;
    private Float capacidadCarga;
 
    public CamionDTO() {}
 
    public CamionDTO(String patente, Float capacidadVolumen, Float altura, Float capacidadCarga) {
        this.patente = patente;
        this.capacidadVolumen = capacidadVolumen;
        this.altura = altura;
        this.capacidadCarga = capacidadCarga;
    }
 
    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }
    public Float getCapacidadVolumen() { return capacidadVolumen; }
    public void setCapacidadVolumen(Float capacidadVolumen) { this.capacidadVolumen = capacidadVolumen; }
    public Float getAltura() { return altura; }
    public void setAltura(Float altura) { this.altura = altura; }
    public Float getCapacidadCarga() { return capacidadCarga; }
    public void setCapacidadCarga(Float capacidadCarga) { this.capacidadCarga = capacidadCarga; }
}
 