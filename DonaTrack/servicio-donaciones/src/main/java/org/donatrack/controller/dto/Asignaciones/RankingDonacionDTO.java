package org.donatrack.controller.dto.Asignaciones;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.donatrack.controller.dto.Nombres;
import org.donatrack.dominio.algoritmo.ResultadoAsignacion;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

// Ranking completo de una donación, para el panel lateral del admin.
public record RankingDonacionDTO(
        Long donacionId,
        String descripcion,
        String subcategoriaNombre,
        int cantidadTotal,
        Ranking ranking) {

    public record Ranking(
            EstadoRanking estado,
            LocalDateTime fechaCalculo,
            boolean tieneCoincidencias,
            List<ListaRanking> listas) {
    }

    // tipo: ResultadoAsignacion.LISTA_COINCIDENCIAS o el nombre del algoritmo.
    public record ListaRanking(String tipo, List<Candidato> entidades) {
    }

    public record Candidato(int posicion, Long entidadId, String entidadNombre, String direccion) {
    }

    // resultado puede ser null: la donación todavía no tiene ranking.
    public static RankingDonacionDTO de(Donacion donacion, ResultadoAsignacion resultado) {
        return new RankingDonacionDTO(
                donacion.getId(),
                donacion.getDescripcion(),
                donacion.getSubcategoria() != null ? donacion.getSubcategoria().getNombre() : null,
                donacion.getCantidadTotal(),
                ranking(resultado));
    }

    // Con coincidencias se muestra solo esa lista (la consigna pide filtrar las entidades que
    // aparecen en ambos algoritmos). Sin coincidencias, una lista por algoritmo.
    private static Ranking ranking(ResultadoAsignacion resultado) {
        if (resultado == null) {
            return new Ranking(EstadoRanking.PENDIENTE, null, false, List.of());
        }

        List<ListaRanking> listas = new ArrayList<>();
        if (resultado.tieneCoincidencias()) {
            listas.add(lista(ResultadoAsignacion.LISTA_COINCIDENCIAS, resultado.getCoincidencias()));
        } else {
            for (Map.Entry<String, List<EntidadBeneficiaria>> entrada : resultado.getRankingsPorAlgoritmo().entrySet()) {
                listas.add(lista(entrada.getKey(), entrada.getValue()));
            }
        }

        return new Ranking(EstadoRanking.CALCULADO, resultado.getFechaCalculo(), resultado.tieneCoincidencias(), listas);
    }

    private static ListaRanking lista(String tipo, List<EntidadBeneficiaria> entidades) {
        List<Candidato> candidatos = new ArrayList<>();
        for (int i = 0; i < entidades.size(); i++) {
            EntidadBeneficiaria entidad = entidades.get(i);
            candidatos.add(new Candidato(i + 1, entidad.getId(), Nombres.deEntidad(entidad), entidad.getDireccion()));
        }
        return new ListaRanking(tipo, candidatos);
    }
}
