package org.donatrack.dominio.algoritmo;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.EstadoDonacion;
import org.donatrack.dominio.donacion.EstadoEntregada;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

public class PrioridadSubatendidos implements AlgoritmoAsignacion {

    private static final int MESES_TRIMESTRE = 3;

    @Override
    public List<EntidadBeneficiaria> generarRanking(Donacion donacion, List<EntidadBeneficiaria> entidades) {
        LocalDateTime limiteTrimestre = LocalDateTime.now().minusMonths(MESES_TRIMESTRE);

        return entidades.stream()
                .sorted(Comparator.comparingInt(entidad -> contarDonacionesEnTrimestre(entidad, limiteTrimestre)))
                .limit(10)   // la consigna pide hasta 10 entidades
                .toList();
    }

    private int contarDonacionesEnTrimestre(EntidadBeneficiaria entidad, LocalDateTime limite) {
        int contador = 0;

        for (Donacion donacion : entidad.getDonacionesRecibidas()) {
            EstadoDonacion estado = donacion.getEstadoDonacion();

            
            if (estado instanceof EstadoEntregada) {
                LocalDateTime fechaEntrega = estado.getDate();

                
                if (fechaEntrega != null && fechaEntrega.isAfter(limite)) {
                    contador++;
                }
            }
        }

        return contador;
    }
}