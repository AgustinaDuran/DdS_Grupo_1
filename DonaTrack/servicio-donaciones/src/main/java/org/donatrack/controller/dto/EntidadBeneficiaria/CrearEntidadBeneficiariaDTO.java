package org.donatrack.controller.dto.EntidadBeneficiaria;

import org.donatrack.dominio.entidadBeneficiaria.TipoEntidadBeneficiaria;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
public class CrearEntidadBeneficiariaDTO {

    private TipoEntidadBeneficiaria tipoEntidad;
    private String direccion;
    private Organizacion organizacion;

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

    public Organizacion getOrganizacion(){
        return organizacion;
    }

    public void setOrganizacion(Organizacion org){
        this.organizacion = org;
    }
}
