package org.donatrack.service;

import org.donatrack.model.*;
import org.donatrack.repository.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
        
        GenerarRankingDe(YearMonth.now().minusMonths(1));
    }
    public RankingMensual GenerarRankingDe(YearMonth periodo) {
        
        Iterable<DonanteIncentivos> todos = donanteRepository.findAll();
        List<DonanteIncentivos> listaDonantes = new ArrayList<>();
        todos.forEach(listaDonantes::add);

        Map<String, Integer> misionesPorDonante = new HashMap<>();
        for (DonanteIncentivos donante : listaDonantes) {
            misionesPorDonante.put(donante.GetNombreUsuario(), donante.CalcularMisionesCumplidasEn(periodo));
        }

        listaDonantes.sort((d1, d2) -> Integer.compare(misionesPorDonante.get(d2.GetNombreUsuario()), misionesPorDonante.get(d1.GetNombreUsuario())));

        RankingMensual rankingDelMes = new RankingMensual(periodo);

        Integer limitePodio = Math.min(listaDonantes.size(), 3);
        for (Integer i = 0; i < limitePodio; i++) {
            DonanteIncentivos donanteGanador = listaDonantes.get(i);
            Integer misionesCount = donanteGanador.CalcularMisionesCumplidasEn(periodo);
            rankingDelMes.AgregarAlPodio(new PuestoRanking(i + 1, donanteGanador.GetNombreUsuario(), misionesCount));
        }

        return rankingRepository.save(rankingDelMes);
    }
}