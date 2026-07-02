package org.donatrack.dominio.usuario;

import org.donatrack.dominio.contacto.Contacto;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DatosUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    protected List<Contacto> contactos;

    protected RolUsuario rolUsuario;

    public DatosUsuario() {
        this.contactos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void agregarContacto(Contacto contacto) {
        this.contactos.add(contacto);
    }
}
