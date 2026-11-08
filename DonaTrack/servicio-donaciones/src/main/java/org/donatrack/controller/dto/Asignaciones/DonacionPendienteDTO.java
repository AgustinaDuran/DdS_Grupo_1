package org.donatrack.controller.dto.Asignaciones;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.donatrack.dominio.algoritmo.ResultadoAsignacion;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

// Fila del listado de donaciones pendientes de asignación, con un resumen de su ranking.
public record DonacionPendienteDTO(
        Long donacionId,
        String descripcion,
        String subcategoriaNombre,
        int cantidadTotal,
        LocalDateTime fechaIngreso,
        LocalDate fechaVencimiento,
        String donanteNombre,
        ResumenRanking ranking) {

    public record ResumenRanking(
            EstadoRanking estado,
            boolean tieneCoincidencias,
            LocalDateTime fechaCalculo,
            Sugerencia sugerenciaPrincipal) {
    }

    public record Sugerencia(Long entidadId, String entidadNombre) {
    }

    // resultado puede ser null: la donación todavía no tiene ranking.
    public static DonacionPendienteDTO de(Donacion donacion, ResultadoAsignacion resultado) {
        return new DonacionPendienteDTO(
                donacion.getId(),
                donacion.getDescripcion(),
                donacion.getSubcategoria() != null ? donacion.getSubcategoria().getNombre() : null,
                donacion.getCantidadTotal(),
                donacion.getFechaIngreso(),
                null, // TODO fase 6: fechaVencimiento cuando Donacion la tenga
                Nombres.deDonante(donacion.getDonante()),
                resumen(resultado));
    }

    private static ResumenRanking resumen(ResultadoAsignacion resultado) {
        if (resultado == null) {
            return new ResumenRanking(EstadoRanking.PENDIENTE, false, null, null);
        }

        // La sugerencia principal es la primera coincidencia. Sin coincidencias no hay una
        // entidad "mejor": el admin elige entre las listas de cada algoritmo.
        Sugerencia sugerencia = null;
        List<EntidadBeneficiaria> coincidencias = resultado.getCoincidencias();
        if (!coincidencias.isEmpty()) {
            EntidadBeneficiaria primera = coincidencias.get(0);
            sugerencia = new Sugerencia(primera.getId(), Nombres.deEntidad(primera));
        }

        return new ResumenRanking(
                EstadoRanking.CALCULADO,
                resultado.tieneCoincidencias(),
                resultado.getFechaCalculo(),
                sugerencia);
    }
}
