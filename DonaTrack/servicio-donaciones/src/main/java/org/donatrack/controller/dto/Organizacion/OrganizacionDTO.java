package org.donatrack.controller.dto.Organizacion;

import org.donatrack.dominio.usuario.organizacion.Organizacion;

public class OrganizacionDTO {

    private String razonSocial;
    private String cuit;

    public OrganizacionDTO() {
    }

    public OrganizacionDTO(String razonSocial, String cuit) {
        this.razonSocial = razonSocial;
        this.cuit = cuit;
    }

    public OrganizacionDTO(Organizacion organizacion) {
        this.razonSocial = organizacion.getRazonSocial();
        this.cuit = organizacion.getCuit();
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public Organizacion toOrganizacion() {
        return new Organizacion(razonSocial, cuit);
    }
}
