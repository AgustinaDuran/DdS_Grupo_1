package org.donatrack.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.IntStream;

import org.donatrack.model.DonacionRegistrada;
import org.donatrack.model.DonanteIncentivos;
import org.donatrack.model.PuestoRanking;
import org.donatrack.model.RankingMensual;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Verifica los mapeos JPA contra una base embebida (H2). Ojo: las donaciones de estos tests
 * se arman de modo que no se cumpla ninguna misión, porque una insignia ganada sin haber sido
 * guardada antes (la fábrica no corre en este slice) rompería el flush por ser transient.
 */
@DataJpaTest
class PersistenciaIncentivosTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private DonanteRepository donanteRepository;
    @Autowired
    private RankingRepository rankingRepository;

    @Test
    void persisteYRelееElDonanteConSusDonacionesYBienes() {
        DonanteIncentivos ana = new DonanteIncentivos("ana");
        ana.RegistrarActividad(donacion("ana", 1, LocalDate.of(2026, 6, 5)));
        em.persist(ana);
        em.flush();
        em.clear();

        DonanteIncentivos leida = donanteRepository.findById("ana").orElseThrow();

        assertThat(leida.GetNombreCategoriaActual()).isEqualTo("Colaborador");
        assertThat(leida.CalcularTotalDonaciones()).isEqualTo(1);
        assertThat(leida.CalcularImpactoAcumulado()).isEqualTo(1);
        assertThat(leida.GetDonaciones()).hasSize(1);
        DonacionRegistrada d = leida.GetDonaciones().get(0);
        assertThat(d.GetBienes()).containsExactly("bien-0");
        assertThat(d.GetCantidadBienes()).isEqualTo(1);
        assertThat(d.GetPeriodo()).isEqualTo(YearMonth.of(2026, 6));
    }

    @Test
    void laPosicionEnElRankingCuentaLosDonantesConMasDonaciones() {
        guardarDonanteConDonaciones("ana", 2);
        guardarDonanteConDonaciones("beto", 1);
        guardarDonanteConDonaciones("carla", 0);

        assertThat(donanteRepository.countDonantesConMasDonaciones("ana")).isEqualTo(0);
        assertThat(donanteRepository.countDonantesConMasDonaciones("beto")).isEqualTo(1);
        assertThat(donanteRepository.countDonantesConMasDonaciones("carla")).isEqualTo(2);
    }

    @Test
    void regenerarUnRankingNoDejaPuestosHuerfanos() {
        RankingMensual primero = new RankingMensual(YearMonth.of(2026, 8));
        primero.AgregarAlPodio(new PuestoRanking(1, "ana", 3));
        primero.AgregarAlPodio(new PuestoRanking(2, "beto", 2));
        rankingRepository.save(primero);
        em.flush();
        em.clear();

        RankingMensual regenerado = new RankingMensual(YearMonth.of(2026, 8));
        regenerado.AgregarAlPodio(new PuestoRanking(1, "carla", 4));
        rankingRepository.save(regenerado);
        em.flush();
        em.clear();

        Long puestos = em.getEntityManager()
                .createQuery("select count(p) from PuestoRanking p", Long.class).getSingleResult();
        assertThat(puestos).isEqualTo(1L);
        assertThat(rankingRepository.findById("2026-08").orElseThrow().GetPodio()).hasSize(1);
    }

    private void guardarDonanteConDonaciones(String usuario, int cantidadDonaciones) {
        DonanteIncentivos donante = new DonanteIncentivos(usuario);
        // Sin bienes, misma subcategoría, organización y mes: no se cumple ninguna misión.
        for (int i = 0; i < cantidadDonaciones; i++) {
            donante.RegistrarActividad(donacion(usuario, 0, LocalDate.of(2026, 6, 5)));
        }
        em.persist(donante);
        em.flush();
    }

    private DonacionRegistrada donacion(String usuario, int cantidadBienes, LocalDate fecha) {
        List<String> bienes = IntStream.range(0, cantidadBienes).mapToObj(i -> "bien-" + i).toList();
        return new DonacionRegistrada(usuario, "alimentos", bienes, "Comedor", fecha);
    }
}