package org.donatrack.controller.dto.EntidadBeneficiaria;

import org.donatrack.dominio.entidadBeneficiaria.TipoEntidadBeneficiaria;

public class ActualizarEntidadBeneficiariaDTO {

    private TipoEntidadBeneficiaria tipoEntidad;
    private String direccion;
    private String razonSocial;
    private String cuit;

    public ActualizarEntidadBeneficiariaDTO() {
    }

    public TipoEntidadBeneficiaria getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(TipoEntidadBeneficiaria tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
}
