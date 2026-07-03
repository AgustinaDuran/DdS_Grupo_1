package org.donatrack.dominio.donante;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.EstadoDonacion;


import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.usuario.DatosUsuario;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Donante {
    protected List<Donacion>  donacionesHistoricas = new ArrayList<>();
    protected DatosUsuario datosUsuario;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public Donante(DatosUsuario datosUsuario) {
        this.datosUsuario = datosUsuario;
        this.donacionesHistoricas = new ArrayList<>();
    }


    public List<Donacion> getDonacionesHistoricas() {
        return donacionesHistoricas;
    }


    public List<Donacion> filtrarPorEstado(EstadoDonacion estado){
        List<Donacion> donaciones = donacionesHistoricas.stream()
                .filter(d -> d.getEstadoDonacion() == estado)
                .toList();
        // System.out.println("Donaciones filtradas por " + estado.name());

        // for(Donacion donacion: donaciones) {
        //     System.out.println("Donacion: " + donacion);
        // }
        return donaciones;
    }

    public List<Donacion> filtrarPorSubcategoria(Subcategoria subcategoria){
        List<Donacion> donaciones = donacionesHistoricas.stream()
                .filter(d -> d.getSubcategoria() == subcategoria)
                .toList();

        // System.out.println("Donaciones filtradas por " + subcategoria.getNombre());

        // for(Donacion donacion: donaciones) {
        //     System.out.println("Donacion: " + donacion);
        // }

        return donaciones;
    }

    public void visualizarDonaciones() {

        System.out.println("Donaciones historicas");

        for(Donacion donacion: donacionesHistoricas) {
            System.out.println("Donacion: " + donacion);
        }
    }

    public void agregarDonacionHistorica(Donacion donacion){
        this.donacionesHistoricas.add(donacion);
    }

    public DatosUsuario getDatosUsuario(){
        return datosUsuario;
    }
    
    public void setDatosUsuario(DatosUsuario d){
        this.datosUsuario = d;
    }
    
}
