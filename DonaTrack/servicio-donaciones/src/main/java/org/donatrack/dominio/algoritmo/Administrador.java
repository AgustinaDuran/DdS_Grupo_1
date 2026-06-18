package org.donatrack.dominio.algoritmo;

import org.donatrack.service.DonacionesService;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Administrador{

    private DonacionesService donacionesService;

    public Administrador(DonacionesService service){
        this.donacionesService = service;
    }

    public void confirmarDestinoFinal(ResultadoAsignacion resultado,EntidadBeneficiaria entidad ){
        if(resultado.tieneCoincidencias()){
            resultado.getCoincidencias().stream()
                                        .filter(e -> e.equals(entidad))
                                        .findFirst()
                                        .orElseThrow(() ->
                                        new IllegalArgumentException("La entidad no pertenece al ranking"));

        donacionesService.asignarDonacion(entidad, resultado.getDonacion());

        }
        
        else{
            resultado.getRankingsPorAlgoritmo()
                     .stream()
                     .flatMap(List::stream)                     
                     .filter(e -> e.equals(entidad))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("La entidad no pertenece al ranking"));

            resultado.getDonacion().asignar(entidad);
        }

    }

    public void marcarComoVencida(Donacion d){
        d.marcarComoVencida();
    }
}