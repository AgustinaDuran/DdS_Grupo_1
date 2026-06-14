package org.donatrack.dominio;

import java.util.ArrayList;
import java.util.List;

public abstract class Donante {
    protected List<Donacion>  donacionesHistoricas = new ArrayList<>();


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
    
}
