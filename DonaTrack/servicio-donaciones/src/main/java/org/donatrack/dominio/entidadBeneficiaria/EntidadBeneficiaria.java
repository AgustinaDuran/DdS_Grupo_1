package org.donatrack.dominio.entidadBeneficiaria;

import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.necesidades.Necesidad;
import org.donatrack.dominio.usuario.organizacion.Organizacion;

import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;


@Entity
@Table(name = "entidades_beneficiarias")
public class EntidadBeneficiaria {
    
    @Enumerated(EnumType.STRING)
    private TipoEntidadBeneficiaria tipoEntidad;
    private String direccion;

    @OneToMany(mappedBy = "entidad")
    private List<Necesidad> necesidades;

    @OneToMany(mappedBy = "entidadAEntregar")
    private List<Donacion> donacionesRecibidas;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizacion_id")
    private Organizacion organizacion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected EntidadBeneficiaria() {
        this.necesidades = new ArrayList<>();
        this.donacionesRecibidas = new ArrayList<>();
    }


    public EntidadBeneficiaria(TipoEntidadBeneficiaria tipoEntidad, String direccion, Organizacion organizacion) {
        this.tipoEntidad = tipoEntidad;
        this.direccion = direccion;
        this.necesidades = new ArrayList<>();
        this.donacionesRecibidas = new ArrayList<>();
        this.organizacion = organizacion;
    }
    
    public TipoEntidadBeneficiaria getTipoEntidad() { 
        return tipoEntidad; }

    public String getDireccion() {
        return direccion; }

    public Organizacion getOrganizacion() {
        return organizacion; }

    public List<Necesidad> getNecesidades() { 
        return necesidades; }

    public void auditarEntidad(){

    }
    
    public void registrarNecesidad(Necesidad necesidad){
        necesidades.add(necesidad);
    }

    public List<Donacion> getDonacionesRecibidas() {    
    return donacionesRecibidas;
    }
    
    public void agregarDonacionRecibida(Donacion donacion) { 
    donacionesRecibidas.add(donacion);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
