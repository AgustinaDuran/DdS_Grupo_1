package org.donatrack.service;

import org.donatrack.model.*;
import org.donatrack.repository.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

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

        YearMonth mesPasado = YearMonth.now().minusMonths(1);
        
        Iterable<DonanteIncentivos> todos = donanteRepository.findAll();
        List<DonanteIncentivos> listaDonantes = new ArrayList<>();
        todos.forEach(listaDonantes::add);

        listaDonantes.sort((d1, d2) -> Integer.compare(
            d2.calcularMisionesCumplidasEn(mesPasado), 
            d1.calcularMisionesCumplidasEn(mesPasado)
        ));

        RankingMensual rankingDelMes = new RankingMensual(mesPasado);

        Integer limitePodio = Math.min(listaDonantes.size(), 3);
        for (Integer i = 0; i < limitePodio; i++) {
            DonanteIncentivos donanteGanador = listaDonantes.get(i);
            Integer misionesCount = donanteGanador.calcularMisionesCumplidasEn(mesPasado);
            
            PuestoRanking puesto = new PuestoRanking(i + 1, donanteGanador.getNombreUsuario(), misionesCount);
            rankingDelMes.agregarAlPodio(puesto);
        }

        rankingRepository.save(rankingDelMes);
    }
}