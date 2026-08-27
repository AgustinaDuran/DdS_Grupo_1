package org.donatrack.service;

import org.donatrack.model.*;
import org.donatrack.repository.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RankingProgramadoService {

    private final DonanteRepository donanteRepository;
    private final RankingRepository rankingRepository;

    public RankingProgramadoService(DonanteRepository donanteRepository, RankingRepository rankingRepository) {
        this.donanteRepository = donanteRepository;
        this.rankingRepository = rankingRepository;
    }

    @Scheduled(cron = "0 0 0 1 * ?") // 0 segs - 0 mins - 0 hs - dia 1 - cada mes - cualquier dia
    public void ejecutarProcesamientoMensual() {
        // Al cierre de cada mes se persiste el ranking del mes que terminó.
        procesarPeriodo(YearMonth.now().minusMonths(1));
    }

    /**
     * Calcula y persiste el ranking de un período. Se expone además a demanda porque el cron
     * sólo corre el día 1 a medianoche y sin esto el podio no se puede demostrar.
     *
     * Es transaccional porque la colección de donaciones del donante es LAZY y el hilo del
     * scheduler no tiene el interceptor web que mantiene la sesión abierta.
     */
    @Transactional
    public RankingMensual procesarPeriodo(YearMonth periodo) {

        YearMonth mesPasado = periodo;

        Iterable<DonanteIncentivos> todos = donanteRepository.findAll();
        List<DonanteIncentivos> listaDonantes = new ArrayList<>();
        todos.forEach(listaDonantes::add);

        Map<String, Integer> misionesPorDonante = new HashMap<>();
        for (DonanteIncentivos donante : listaDonantes) {
            misionesPorDonante.put(donante.GetNombreUsuario(), donante.CalcularMisionesCumplidasEn(mesPasado));
        }

        listaDonantes.sort((d1, d2) -> Integer.compare(misionesPorDonante.get(d2.GetNombreUsuario()), misionesPorDonante.get(d1.GetNombreUsuario())));

        RankingMensual rankingDelMes = new RankingMensual(mesPasado);

        Integer limitePodio = Math.min(listaDonantes.size(), 3);
        for (Integer i = 0; i < limitePodio; i++) {
            DonanteIncentivos donanteGanador = listaDonantes.get(i);
            Integer misionesCount = donanteGanador.CalcularMisionesCumplidasEn(mesPasado);
            
            PuestoRanking puesto = new PuestoRanking(i + 1, donanteGanador.GetNombreUsuario(), misionesCount);
            rankingDelMes.AgregarAlPodio(puesto);
        }

        return rankingRepository.save(rankingDelMes);
    }
}