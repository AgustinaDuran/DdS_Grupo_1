package org.donatrack.integracion.dto;

import java.time.LocalDate;
import java.util.List;

public class RegistrarDonacionRequest {
    private String subcategoria;
    private List<String> bienes;
    private String organizacion;
    private LocalDate fechaIngreso;
    private List<ContactoNotificacion> contactos;

    public RegistrarDonacionRequest() {
    }

    public RegistrarDonacionRequest(String subcategoria, List<String> bienes, String organizacion,
                                    LocalDate fechaIngreso) {
        this(subcategoria, bienes, organizacion, fechaIngreso, null);
    }

    public RegistrarDonacionRequest(String subcategoria, List<String> bienes, String organizacion,
                                    LocalDate fechaIngreso, List<ContactoNotificacion> contactos) {
        this.subcategoria = subcategoria;
        this.bienes = bienes;
        this.organizacion = organizacion;
        this.fechaIngreso = fechaIngreso;
        this.contactos = contactos;
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

    public List<ContactoNotificacion> getContactos() {
        return contactos;
    }

    public void setContactos(List<ContactoNotificacion> contactos) {
        this.contactos = contactos;
    }
}
