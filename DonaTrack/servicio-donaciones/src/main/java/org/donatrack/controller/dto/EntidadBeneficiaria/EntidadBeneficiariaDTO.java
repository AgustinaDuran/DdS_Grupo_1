package org.donatrack.controller.dto.EntidadBeneficiaria;

import org.donatrack.controller.dto.Organizacion.RepresentanteDTO;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.entidadBeneficiaria.TipoEntidadBeneficiaria;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.dominio.usuario.persona.Persona;

import java.util.ArrayList;
import java.util.List;

public class EntidadBeneficiariaDTO {

    private Long id;
    private TipoEntidadBeneficiaria tipoEntidad;
    private String direccion;
    private String razonSocial;
    private List<RepresentanteDTO> representantes;

    public EntidadBeneficiariaDTO(EntidadBeneficiaria entidad) {
        this.id = entidad.getId();
        this.tipoEntidad = entidad.getTipoEntidad();
        this.direccion = entidad.getDireccion();

        Organizacion organizacion = entidad.getOrganizacion();
        this.representantes = new ArrayList<>();
        if (organizacion != null) {
            this.razonSocial = organizacion.getRazonSocial();
            if (organizacion.getRepresentantes() != null) {
                for (Persona representante : organizacion.getRepresentantes()) {
                    this.representantes.add(new RepresentanteDTO(representante));
                }
            }
        }
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

    public String getRazonSocial() {
        return razonSocial;
    }

    public List<RepresentanteDTO> getRepresentantes() {
        return representantes;
    }
}
