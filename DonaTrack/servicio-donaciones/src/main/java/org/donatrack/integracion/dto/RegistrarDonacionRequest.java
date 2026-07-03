package org.donatrack.integracion.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Payload que consume el Servicio de Incentivos en
 * POST /api/incentivos/registrar-donacion?nombreUsuario=... (cuerpo = esta clase).
 */
public class RegistrarDonacionRequest {
    private String subcategoria;
    private List<String> bienes;
    private String organizacion;
    private LocalDate fechaIngreso;

    public RegistrarDonacionRequest() {
    }

    public RegistrarDonacionRequest(String subcategoria, List<String> bienes, String organizacion,
                                    LocalDate fechaIngreso) {
        this.subcategoria = subcategoria;
        this.bienes = bienes;
        this.organizacion = organizacion;
        this.fechaIngreso = fechaIngreso;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public List<String> getBienes() {
        return bienes;
    }

    public void setBienes(List<String> bienes) {
        this.bienes = bienes;
    }

    public String getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(String organizacion) {
        this.organizacion = organizacion;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}
