package org.donatrack.dominio.entidadBeneficiaria;

import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.necesidades.Necesidad;
import org.donatrack.dominio.usuario.organizacion.Organizacion;

import org.springframework.data.annotation.Id;
import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;


public class EntidadBeneficiaria {
    
    private TipoEntidadBeneficiaria tipoEntidad;
    private String direccion;
    private List<Necesidad> necesidades;
    private List<Donacion> donacionesRecibidas;
    private Organizacion organizacion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


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

    public List<Necesidad> getNecesidades() { 
        return necesidades; }

    public void auditarEntidad(){ //para un futuro

    }
    
    public void registrarNecesidad(Necesidad necesidad){
        necesidades.add(necesidad);
    }

    /* public void confirmarRecepcionDonacion(Necesidad necesidad){ //confirma cuando una necesidad es saldada
        necesidad.marcarComoSaldada();
    }

    public void verEstadoDonaciones(List<Donacion> donaciones){

       for (Donacion donacion : donaciones){
            System.out.println(donacion.getDescripcion() + " -> " + donacion.getEstadoDonacion());
        }
    }

    public void seguirEntregasActivas(List<Donacion> donaciones) {
        for (Donacion donacion : donaciones) {
            if (donacion.getEstadoDonacion().entregaActiva()) {
                System.out.println("Entrega activa: "+ donacion.getDescripcion());
            }
        }
    }

    public void recibirNotificacion(String mensaje){
        System.out.println("Notificación: " + mensaje);
    }
 */
    public List<Donacion> getDonacionesRecibidas() {    
    return donacionesRecibidas;
    }
    
    public void agregarDonacionRecibida(Donacion donacion) { 
    donacionesRecibidas.add(donacion);
    }

    public Long getId() {
        return id;
    }


   /*  public void cargarFotoDonacion(String foto, Donacion donacion){
        GestorDonaciones.agregarFotoEntrega(foto, donacion);
    } es algo que hacen los service, clase del dominio no manda a los service*/

}

