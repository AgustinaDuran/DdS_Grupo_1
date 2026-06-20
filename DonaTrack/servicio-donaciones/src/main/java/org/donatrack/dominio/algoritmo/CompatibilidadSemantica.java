package org.donatrack.dominio.algoritmo;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.necesidades.*;
import org.donatrack.dominio.categoria.*;

import org.springframework.stereotype.Component;
 
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;


@Component
public class CompatibilidadSemantica implements AlgoritmoAsignacion {
 
    private static final int MAX_ENTIDADES = 10;
 
    @Override
    public List<EntidadBeneficiaria> generarRanking(Donacion donacion, List<EntidadBeneficiaria> entidades) {
        Subcategoria subcategoriaDonacion = donacion.getSubcategoria();
 
        return entidades.stream()
                .filter(entidad -> tieneNecesidadActivaCompatible(entidad, subcategoriaDonacion))
                .sorted(Comparator.comparingInt(
                        (EntidadBeneficiaria entidad) -> puntajeCompatibilidad(entidad, subcategoriaDonacion)
                ).reversed())
                .limit(MAX_ENTIDADES)
                .toList();
    }
 
    /**
     * Verifica si la entidad tiene al menos una necesidad activa
     * cuya subcategoría coincida con la de la donación.
     */
    private boolean tieneNecesidadActivaCompatible(EntidadBeneficiaria entidad, Subcategoria subcategoria) {
        return entidad.getNecesidades().stream()
                .anyMatch(necesidad -> necesidad.isActiva() && necesidad.getSubcategoria().equals(subcategoria));
    }
 
    /**
     * Cuenta cuántas necesidades activas de la entidad coinciden
     * con la subcategoría donada. A mayor cantidad, mayor prioridad.
     */
    private int puntajeCompatibilidad(EntidadBeneficiaria entidad, Subcategoria subcategoria) {
        return entidad.getNecesidades().stream()
                .filter(necesidad -> necesidad.isActiva() && necesidad.getSubcategoria().equals(subcategoria))
                .mapToInt(Necesidad::getCantidad)
                .sum();
    }
    }