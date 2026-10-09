package org.donatrack.dominio.usuario;

import org.donatrack.dominio.contacto.Contacto;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DatosUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    protected List<Contacto> contactos;

    @Enumerated(EnumType.STRING)
    protected RolUsuario rolUsuario;

    public DatosUsuario() {
        this.contactos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void agregarContacto(Contacto contacto) {
        this.contactos.add(contacto);
    }
}
