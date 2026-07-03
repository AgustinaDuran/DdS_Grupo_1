package org.donatrack.integracion;

import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.dominio.usuario.persona.Persona;
import org.donatrack.integracion.dto.ContactoNotificacion;
import org.donatrack.integracion.dto.NotificacionRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Traduce entidades de dominio (donantes / entidades beneficiarias) a los datos de
 * destinatario que necesita el Servicio de Notificaciones, y deriva el nombre de usuario
 * que consume el Servicio de Incentivos.
 */
@Component
public class DestinatarioResolver {

    public NotificacionRequest paraDonante(Donante donante, String mensaje) {
        DatosUsuario datos = donante.getDatosUsuario();
        return new NotificacionRequest(
                donante.getId(),
                nombreDe(datos),
                mensaje,
                contactosDe(datos));
    }

    public NotificacionRequest paraEntidad(EntidadBeneficiaria entidad, String mensaje) {
        Organizacion organizacion = entidad.getOrganizacion();
        String nombre = organizacion != null ? organizacion.getRazonSocial() : "Entidad beneficiaria";
        return new NotificacionRequest(
                entidad.getId(),
                nombre,
                mensaje,
                contactosDe(organizacion));
    }

    /** Nombre de usuario usado como clave en el Servicio de Incentivos. */
    public String nombreUsuarioDe(Donante donante) {
        return nombreDe(donante.getDatosUsuario());
    }

    private String nombreDe(DatosUsuario datos) {
        if (datos instanceof Persona persona) {
            return (persona.getNombre() + " " + persona.getApellido()).trim();
        }
        if (datos instanceof Organizacion organizacion) {
            return organizacion.getRazonSocial();
        }
        return "Usuario";
    }

    private List<ContactoNotificacion> contactosDe(DatosUsuario datos) {
        List<ContactoNotificacion> resultado = new ArrayList<>();
        if (datos == null) {
            return resultado;
        }
        if (datos instanceof Persona persona) {
            agregar(resultado, persona.getContactoPredeterminado());
        }
        if (datos.getContactos() != null) {
            for (Contacto contacto : datos.getContactos()) {
                agregar(resultado, contacto);
            }
        }
        return resultado;
    }

    private void agregar(List<ContactoNotificacion> destino, Contacto contacto) {
        if (contacto == null || contacto.getMedio() == null || contacto.getValor() == null) {
            return;
        }
        boolean yaEsta = destino.stream()
                .anyMatch(c -> c.getValor().equalsIgnoreCase(contacto.getValor()));
        if (!yaEsta) {
            destino.add(new ContactoNotificacion(contacto.getMedio(), contacto.getValor()));
        }
    }
}
