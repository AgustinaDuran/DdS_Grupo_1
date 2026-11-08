package org.donatrack.dominio.algoritmo;

import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.TipoEstado;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.repository.DonacionesRepository;
import org.donatrack.repository.EntidadesBeneficiariasRepository;
import org.donatrack.repository.ResultadoAsignacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CoordinadorAsignacion {

    private final List<AlgoritmoAsignacion> algoritmos;
    private final EntidadesBeneficiariasRepository entidadesRepository;
    private final DonacionesRepository donacionesRepository;
    private final ResultadoAsignacionRepository resultadosRepository;

    // Spring inyecta en "algoritmos" todos los beans que implementan AlgoritmoAsignacion.
    public CoordinadorAsignacion(List<AlgoritmoAsignacion> algoritmos,
                                 EntidadesBeneficiariasRepository entidadesRepository,
                                 DonacionesRepository donacionesRepository,
                                 ResultadoAsignacionRepository resultadosRepository) {
        this.algoritmos = algoritmos;
        this.entidadesRepository = entidadesRepository;
        this.donacionesRepository = donacionesRepository;
        this.resultadosRepository = resultadosRepository;
    }

    // Calcula y guarda el ranking de cada donación en depósito.
    // @Transactional: todo se guarda junto o nada, y los algoritmos pueden recorrer las
    // relaciones lazy (necesidades, donaciones recibidas) aunque el job corra fuera de un request.
    @Transactional
    public List<ResultadoAsignacion> generarRecomendaciones() {
        borrarResultadosObsoletos();

        List<EntidadBeneficiaria> entidades = entidadesRepository.findAll();
        List<Donacion> enDeposito = donacionesRepository.buscarConFiltros(TipoEstado.EN_DEPOSITO, null, null, null);

        // Los resultados que ya existen, en una sola consulta, para recalcularlos en lugar de crear otros.
        Map<Long, ResultadoAsignacion> existentes = resultadosRepository
                .findByDonacionIdIn(enDeposito.stream().map(Donacion::getId).toList())
                .stream()
                .collect(Collectors.toMap(r -> r.getDonacion().getId(), r -> r));

        List<ResultadoAsignacion> resultados = new ArrayList<>();
        for (Donacion donacion : enDeposito) {
            resultados.add(generarRecomendacion(donacion, entidades, existentes.get(donacion.getId())));
        }

        return resultadosRepository.saveAll(resultados);
    }

    // Borra los rankings de donaciones que ya no están en depósito (asignadas o vencidas).
    // Se cargan y se borran con deleteAll para que el cascade también borre sus ítems.
    private void borrarResultadosObsoletos() {
        resultadosRepository.deleteAll(resultadosRepository.findByDonacionFueraDeEstado(TipoEstado.EN_DEPOSITO));
    }

    private ResultadoAsignacion generarRecomendacion(Donacion donacion, List<EntidadBeneficiaria> entidades,
                                                     ResultadoAsignacion existente) {
        Map<String, List<EntidadBeneficiaria>> rankings = new LinkedHashMap<>();
        for (AlgoritmoAsignacion algoritmo : algoritmos) {
            rankings.put(algoritmo.nombre(), algoritmo.generarRanking(donacion, entidades));
        }

        List<EntidadBeneficiaria> coincidencias = interseccion(new ArrayList<>(rankings.values()));

        // Si ya había un ranking, se actualiza la misma fila: borrar y crear otro en la misma
        // transacción chocaría con el UNIQUE de donacion_id (Hibernate inserta antes de borrar).
        if (existente != null) {
            existente.recalcular(rankings, coincidencias);
            return existente;
        }
        return new ResultadoAsignacion(donacion, rankings, coincidencias);
    }

    public List<EntidadBeneficiaria> interseccion(List<List<EntidadBeneficiaria>> rankings) {
        if (rankings.isEmpty()) {
            return new ArrayList<>();
        }

        Set<EntidadBeneficiaria> enTodas = new HashSet<>(rankings.get(0));
        for (List<EntidadBeneficiaria> ranking : rankings) {
            enTodas.retainAll(ranking);
        }

        return ordenarInterseccion(enTodas, rankings);
    }

    private List<EntidadBeneficiaria> ordenarInterseccion(Collection<EntidadBeneficiaria> coincidencias,
                                                          List<List<EntidadBeneficiaria>> rankings) {
        Map<EntidadBeneficiaria, Integer> sumaPosiciones = new HashMap<>();
        for (EntidadBeneficiaria entidad : coincidencias) {
            int suma = 0;
            for (List<EntidadBeneficiaria> ranking : rankings) {
                suma += ranking.indexOf(entidad) + 1;
            }
            sumaPosiciones.put(entidad, suma);
        }

        return coincidencias.stream()
                .sorted(Comparator.comparing((EntidadBeneficiaria e) -> sumaPosiciones.get(e))
                        .thenComparing(EntidadBeneficiaria::getId, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
