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
        return bienes.size();
    }

}
