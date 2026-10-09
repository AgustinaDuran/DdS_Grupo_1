package org.donatrack.dominio.usuario.organizacion;

import org.donatrack.dominio.contacto.*;
import org.donatrack.dominio.necesidades.*;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.entidadBeneficiaria.*;
import org.donatrack.dominio.donante.DonanteJuridico;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.donatrack.dominio.usuario.persona.Persona;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "organizaciones")
public class Organizacion extends DatosUsuario {
    private String razonSocial;
    private String cuit;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "organizacion_representantes",
            joinColumns = @JoinColumn(name = "organizacion_id"),
            inverseJoinColumns = @JoinColumn(name = "persona_id"))
    private List<Persona> representantes = new ArrayList<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "organizacion_roles", joinColumns = @JoinColumn(name = "organizacion_id"))
    private List<RolesOrganizacion> rolesOrganizacion;

    protected Organizacion() {
    }

    public Organizacion(String razonSocial, String cuit) {
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.representantes = new ArrayList<>();
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

    public List<Persona> getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(List<Persona> representantes) {
        this.representantes = representantes;
    }

    public void darAltaRepresentante(Persona representante) {
        this.representantes.add(representante);
    }

    public void darBajaRepresentante(Persona representante) {
        this.representantes.remove(representante);
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }

}
