package org.donatrack.controller.dto.EntidadBeneficiaria;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.entidadBeneficiaria.TipoEntidadBeneficiaria;

public class EntidadBeneficiariaDTO {

    private Long id;
    private TipoEntidadBeneficiaria tipoEntidad;
    private String direccion;

    public EntidadBeneficiariaDTO(EntidadBeneficiaria entidad) {
        this.id = entidad.getId();
        this.tipoEntidad = entidad.getTipoEntidad();
        this.direccion = entidad.getDireccion();
    }

    public Long getId() {
        return id;
    }

    public TipoEntidadBeneficiaria getTipoEntidad() {
        return tipoEntidad;
    }

    public String getDireccion() {
        return direccion;
    }
}
