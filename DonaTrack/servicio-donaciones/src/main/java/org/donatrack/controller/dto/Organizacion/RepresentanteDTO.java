package org.donatrack.controller.dto.Organizacion;

import org.donatrack.controller.dto.Donantes.ContactoDTO;
import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.usuario.persona.Persona;

/**
 * Persona representante designada de una organización. De un representante interesa cómo
 * se llama y por dónde contactarlo: es la dirección a la que se notifican los eventos de
 * la entidad beneficiaria (asignación de donaciones, inicio de ruta y entregas).
 */
public class RepresentanteDTO {

    private String nombre;
    private String apellido;
    private ContactoDTO contacto;

    public RepresentanteDTO() {
    }

    public RepresentanteDTO(String nombre, String apellido, ContactoDTO contacto) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.contacto = contacto;
    }

    public RepresentanteDTO(Persona representante) {
        this.nombre = representante.getNombre();
        this.apellido = representante.getApellido();
        Contacto predeterminado = representante.getContactoPredeterminado();
        if (predeterminado != null) {
            this.contacto = new ContactoDTO(predeterminado.getMedio(), predeterminado.getValor());
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public ContactoDTO getContacto() {
        return contacto;
    }

    public void setContacto(ContactoDTO contacto) {
        this.contacto = contacto;
    }

    public Persona toPersona() {
        Contacto contactoPredeterminado = contacto == null
                ? null
                : new Contacto(contacto.getMedio(), contacto.getValor());
        return new Persona(nombre, apellido, contactoPredeterminado);
    }
}
