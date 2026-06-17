/* package org.donatrack.service;
import org.donatrack.repository.RepositoryDonaciones;
import org.donatrack.dominio.*;


import java.util.ArrayList;
import java.util.List;

public class ServiceDonaciones { 

    private final RepositoryDonaciones repository;


    public ServiceDonaciones(RepositoryDonaciones repositoryDonaciones){
        this.repository = repositoryDonaciones;
    }
 */

    /* public void agregarDonacion(Donacion donacion){
        List<Donacion> donacionesSegmentadas = segmentarDonacion(donacion);
    
        RepositoryDonaciones deposito = RepositoryDonaciones.getInstance();
        deposito.agregarDonaciones(donacionesSegmentadas);
    } */

/*     public List<Donacion> segmentarDonacion(Donacion donacionCompleta){
        List<Donacion> donacionesSegmentadas = new ArrayList<>();
        List<ItemBien> bienesDonacion = donacionCompleta.getItemBienes();
        

        for (ItemBien itemBien : bienesDonacion) {
            Subcategoria subcategoriaBien = itemBien.getBien().getSubcategoria();
            Donacion donacionDeSubcategoria = donacionDeSubcategoria(donacionesSegmentadas, subcategoriaBien);
            
            if(donacionesSegmentadas.isEmpty() || donacionDeSubcategoria == null){
                List<ItemBien> primerBienEnLista = new ArrayList<>();
                primerBienEnLista.add(itemBien);
                Donacion donacionNueva = new Donacion(primerBienEnLista);
                donacionNueva.setSubcategoria(subcategoriaBien);
                donacionesSegmentadas.add(donacionNueva);
            } else{
                donacionDeSubcategoria.agregarItemBien(itemBien);
            }
        }
        
        donacionCompleta = null;
        return donacionesSegmentadas;
    }

    private Donacion donacionDeSubcategoria(List<Donacion> donaciones, Subcategoria subcategoria){
        return donaciones.stream()
            .filter(d -> d.getItemBienes().stream().anyMatch(b -> b.getBien().getSubcategoria() == subcategoria))
            .findFirst()
            .orElse(null);
    }

    public static List<Donacion> getDonaciones() {
        RepositoryDonaciones deposito = RepositoryDonaciones.getInstance();
        return deposito.getDonaciones();
    }

    public static void agregarFotoEntrega(String foto, Donacion donacion) {
        donacion.setFotoEntrega(foto);
    }


} */
