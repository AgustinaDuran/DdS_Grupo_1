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
        recolectarContactos(datos, resultado);
        return resultado;
    }

    /**
     * Acumula sobre una única lista para que agregar(...) pueda deduplicar entre los
     * contactos propios y los de cada representante.
     */
    private void recolectarContactos(DatosUsuario datos, List<ContactoNotificacion> destino) {
        if (datos == null) {
            return;
        }
        if (datos instanceof Persona persona) {
            agregar(destino, persona.getContactoPredeterminado());
        }
        if (datos.getContactos() != null) {
            for (Contacto contacto : datos.getContactos()) {
                agregar(destino, contacto);
            }
        }
        // A una organización se la contacta a través de las personas representantes
        // designadas: son quienes reciben los avisos en nombre de la entidad.
        if (datos instanceof Organizacion organizacion && organizacion.getRepresentantes() != null) {
            for (Persona representante : organizacion.getRepresentantes()) {
                recolectarContactos(representante, destino);
            }
        }
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
