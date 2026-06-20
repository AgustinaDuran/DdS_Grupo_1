package org.donatrack.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NecesidadesService {

    /* public void agregarNecesidad(Necesidad necesidad){
        RepositorioNecesidades.necesidades.add(necesidad);
    }

    public void eliminarNecesidad(Necesidad necesidad){
        RepositorioNecesidades.necesidades.remove(necesidad);
    }

    public Donacion seleccionarDonacionSegun(Necesidad necesidad){
        
        List<Donacion> donaciones = Deposito.getDonaciones();

        for (Donacion donacion : donaciones){
            if (donacion.getSubcategoria().equals(necesidad.getSubcategoria())) {
                if (donacion.getCantidadTotal() >= necesidad.getCantidad()) {
                    return donacion;
                }
            }

        }
        return null;
    } */

}
