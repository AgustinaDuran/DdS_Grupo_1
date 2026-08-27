package org.donatrack.controller.dto;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class DonacionDTO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String nombreUsuario;
    String subcategoria;
    @ElementCollection
    List<String> bienes;
    String organizacion;
    LocalDate fechaIngreso;

    public DonacionDTO() {
    }

    public DonacionDTO(String nombreUsuario, String subcategoria, List<String> bienes, String organizacion, LocalDate fechaIngreso) {
        this.nombreUsuario = nombreUsuario;
        this.subcategoria = subcategoria;
        this.bienes = bienes;
        this.organizacion = organizacion;
        this.fechaIngreso = fechaIngreso;
    }

    public String GetNombreUsuario() { return nombreUsuario; }

    public String GetSubcategoria(){ return subcategoria; }

    public List<String> GetBienes(){ return bienes; }

    public String GetOrganizacion(){ return organizacion; }

    public LocalDate GetFechaIngreso() { return fechaIngreso; }

    public Integer GetCantidadBienes() {
        return bienes == null ? 0 : bienes.size();
    }

    // Accessors JavaBean: Jackson sólo reconoce el prefijo 'get' en minúscula y campos públicos,
    // así que sin estos métodos el JSON entrante llegaba con todos los campos en null y el DTO
    // no se podía serializar en la respuesta del perfil.

    public String getNombreUsuario() { return nombreUsuario; }

    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getSubcategoria() { return subcategoria; }

    public void setSubcategoria(String subcategoria) { this.subcategoria = subcategoria; }

    public List<String> getBienes() { return bienes; }

    public void setBienes(List<String> bienes) { this.bienes = bienes; }

    public String getOrganizacion() { return organizacion; }

    public void setOrganizacion(String organizacion) { this.organizacion = organizacion; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }

    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

}
