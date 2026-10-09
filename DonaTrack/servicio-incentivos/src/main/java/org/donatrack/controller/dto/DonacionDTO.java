package org.donatrack.controller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.donatrack.integracion.dto.ContactoNotificacion;
import org.donatrack.model.DonacionRegistrada;

/**
 * Contrato HTTP de una donación. No se persiste: se convierte a {@link DonacionRegistrada}
 * para el dominio. Los contactos sólo se usan para notificar al donante.
 */
public class DonacionDTO {

    private String nombreUsuario;
    private String subcategoria;
    private List<String> bienes;
    private String organizacion;
    private LocalDate fechaIngreso;
    private List<ContactoNotificacion> contactos;

    public DonacionDTO() { }

    public DonacionRegistrada toDominio() {
        return new DonacionRegistrada(nombreUsuario, subcategoria, bienes, organizacion, fechaIngreso);
    }

    public static DonacionDTO desde(DonacionRegistrada d) {
        DonacionDTO dto = new DonacionDTO();
        dto.nombreUsuario = d.GetNombreUsuario();
        dto.subcategoria = d.GetSubcategoria();
        dto.bienes = d.GetBienes() == null ? new ArrayList<>() : new ArrayList<>(d.GetBienes());
        dto.organizacion = d.GetOrganizacion();
        dto.fechaIngreso = d.GetFechaIngreso();
        return dto;
    }

    // Accessors JavaBean: Jackson sólo reconoce el prefijo 'get'/'set' en minúscula.

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
    public List<ContactoNotificacion> getContactos() { return contactos; }
    public void setContactos(List<ContactoNotificacion> contactos) { this.contactos = contactos; }
}
