package org.donatrack.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.donatrack.controller.dto.DonacionDTO;
import org.junit.jupiter.api.Test;

class DonanteIncentivosTest {

    @Test
    void registraDonacionesYCalculaMetricas() {
        DonanteIncentivos donante = new DonanteIncentivos("ana");
        donante.RegistrarActividad(donacion("ana", "arroz", 3, "Comedor", LocalDate.of(2026, 1, 10)));
        donante.RegistrarActividad(donacion("ana", "fideos", 2, "Escuela", LocalDate.of(2026, 2, 10)));

        assertThat(donante.CalcularTotalDonaciones()).isEqualTo(2);
        assertThat(donante.CalcularDonacionesDistintas()).isEqualTo(2);
        assertThat(donante.CalcularOrganizacionesAyudadas()).isEqualTo(2);
        assertThat(donante.CalcularImpactoAcumulado()).isEqualTo(5);
        assertThat(donante.ObtenerEvolucionDonacionesPorPeriodo(
                java.time.YearMonth.of(2026, 1), java.time.YearMonth.of(2026, 2)))
                .containsExactly(3, 2);
    }

    @Test
    void calculaRachaSinContarDosDonacionesDelMismoMesDosVeces() {
        DonanteIncentivos donante = new DonanteIncentivos("ana");
        donante.RegistrarActividad(donacion("ana", "arroz", 1, "Comedor", LocalDate.of(2026, 1, 3)));
        donante.RegistrarActividad(donacion("ana", "fideos", 1, "Comedor", LocalDate.of(2026, 1, 20)));
        donante.RegistrarActividad(donacion("ana", "aceite", 1, "Comedor", LocalDate.of(2026, 2, 5)));
        donante.RegistrarActividad(donacion("ana", "leche", 1, "Comedor", LocalDate.of(2026, 3, 5)));

        assertThat(donante.CalcularRachaActual()).isEqualTo(3);
        assertThat(donante.GetCategoriaActual().GetMisionesDelNivel().get(0)
                .GetProgresoActual(donante)).isEqualTo(3);
    }

    @Test
    void completarMisionOtorgaInsignia() {
        DonanteIncentivos donante = new DonanteIncentivos("ana");
        donante.RegistrarActividad(donacion("ana", "arroz", 1, "Comedor", LocalDate.of(2026, 1, 3)));
        donante.RegistrarActividad(donacion("ana", "fideos", 1, "Comedor", LocalDate.of(2026, 2, 3)));
        ResultadoActividad resultado = donante.RegistrarActividad(
                donacion("ana", "aceite", 1, "Comedor", LocalDate.of(2026, 3, 3)));

        assertThat(resultado.GetMisionesCumplidas()).hasSize(2);
        assertThat(donante.GetInsigniasGanadas()).hasSize(3);
    }

    private DonacionDTO donacion(String usuario, String subcategoria, int cantidad,
            String organizacion, LocalDate fecha) {
        return new DonacionDTO(usuario, subcategoria,
                java.util.stream.IntStream.range(0, cantidad)
                        .mapToObj(indice -> "bien-" + indice).toList(),
                organizacion, fecha);
    }
}