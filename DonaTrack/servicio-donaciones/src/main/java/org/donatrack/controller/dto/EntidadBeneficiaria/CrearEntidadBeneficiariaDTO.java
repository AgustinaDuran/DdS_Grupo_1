package org.donatrack.controller.dto.EntidadBeneficiaria;

import org.donatrack.dominio.entidadBeneficiaria.TipoEntidadBeneficiaria;

public class CrearEntidadBeneficiariaDTO {

    private TipoEntidadBeneficiaria tipoEntidad;
    private String direccion;

    public CrearEntidadBeneficiariaDTO() {
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
}
