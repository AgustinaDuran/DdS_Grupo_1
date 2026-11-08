package org.donatrack.controller.dto;

import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.dominio.usuario.persona.Persona;

// Nombres para mostrar en la UI, a partir de las entidades del dominio.
public final class Nombres {

    private Nombres() {
    }

    public static String deEntidad(EntidadBeneficiaria entidad) {
        if (entidad == null || entidad.getOrganizacion() == null) {
            return null;
        }
        return entidad.getOrganizacion().getRazonSocial();
    }

    public static String deDonante(Donante donante) {
        if (donante == null) {
            return null;
        }
        DatosUsuario datos = donante.getDatosUsuario();
        if (datos instanceof Persona persona) {
            return (persona.getNombre() + " " + persona.getApellido()).trim();
        }
        if (datos instanceof Organizacion organizacion) {
            return organizacion.getRazonSocial();
        }
        return null;
    }
}
