package org.donatrack;

import org.donatrack.model.PuestoRanking;
import org.donatrack.model.RankingMensual;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifica el armado del podio mensual: identificador de período y puestos.
 */
class RankingMensualTest {

    @Test
    void armaElPodioConLosTresPrimeros() {
        RankingMensual ranking = new RankingMensual(YearMonth.of(2026, 5));

        ranking.agregarAlPodio(new PuestoRanking(1, "ana", 8));
        ranking.agregarAlPodio(new PuestoRanking(2, "luis", 5));
        ranking.agregarAlPodio(new PuestoRanking(3, "marta", 3));

        assertEquals("2026-05", ranking.getIdPeriodo());
        assertEquals(3, ranking.getPodio().size());

        PuestoRanking primero = ranking.getPodio().get(0);
        assertEquals(1, primero.getPosicion());
        assertEquals("ana", primero.getNombreUsuario());
        assertEquals(8, primero.getMisionesCumplidasEnElMes());
    }
}
