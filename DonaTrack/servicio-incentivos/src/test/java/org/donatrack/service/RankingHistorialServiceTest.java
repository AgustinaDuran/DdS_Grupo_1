package org.donatrack.service;

import org.donatrack.model.RankingMensual;
import org.donatrack.repository.DonanteRepository;
import org.donatrack.repository.RankingRepository;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RankingHistorialServiceTest {

    @Test
    void devuelveLosPeriodosProcesadosDelMasRecienteAlMasAntiguo() {
        RankingRepository rankings = mock(RankingRepository.class);
        when(rankings.findAll()).thenReturn(List.of(
                new RankingMensual(YearMonth.of(2026, 1)),
                new RankingMensual(YearMonth.of(2026, 10)),
                new RankingMensual(YearMonth.of(2025, 12))));

        AnaliticaService service = new AnaliticaService(
                mock(DonanteRepository.class), rankings, mock(AutomatizacionService.class));

        assertThat(service.ObtenerPeriodosDeRanking())
                .containsExactly("2026-10", "2026-01", "2025-12");
    }
}
