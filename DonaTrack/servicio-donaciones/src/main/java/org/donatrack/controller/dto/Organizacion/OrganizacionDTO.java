package org.donatrack.controller.dto.Organizacion;

import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.dominio.usuario.persona.Persona;

import java.util.ArrayList;
import java.util.List;

public class OrganizacionDTO {

    private String razonSocial;
    private String cuit;
    private List<RepresentanteDTO> representantes;

    public OrganizacionDTO() {
    }

    public OrganizacionDTO(String razonSocial, String cuit) {
        this.razonSocial = razonSocial;
        this.cuit = cuit;
    }

    public OrganizacionDTO(Organizacion organizacion) {
        this.razonSocial = organizacion.getRazonSocial();
        this.cuit = organizacion.getCuit();
        this.representantes = representantesDe(organizacion);
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

    public List<RepresentanteDTO> getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(List<RepresentanteDTO> representantes) {
        this.representantes = representantes;
    }

    public Organizacion toOrganizacion() {
        Organizacion organizacion = new Organizacion(razonSocial, cuit);
        if (representantes != null) {
            for (RepresentanteDTO representante : representantes) {
                if (representante != null) {
                    organizacion.darAltaRepresentante(representante.toPersona());
                }
            }
        }
        return organizacion;
    }

    private static List<RepresentanteDTO> representantesDe(Organizacion organizacion) {
        List<RepresentanteDTO> resultado = new ArrayList<>();
        if (organizacion.getRepresentantes() == null) {
            return resultado;
        }
        for (Persona representante : organizacion.getRepresentantes()) {
            resultado.add(new RepresentanteDTO(representante));
        }
        return resultado;
    }
}
