package org.donatrack.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DesnormalizacionTest {

    @Test
    void laDonacionGuardaCantidadDeBienesYPeriodoAlConstruirse() {
        DonacionRegistrada d = new DonacionRegistrada("ana", "alimentos", List.of("arroz", "fideos"), "Comedor", LocalDate.of(2026, 6, 5));

        assertThat(ReflectionTestUtils.getField(d, "cantidadBienes")).isEqualTo(2);
        assertThat(ReflectionTestUtils.getField(d, "periodo")).isEqualTo("2026-06");
        assertThat(d.GetCantidadBienes()).isEqualTo(2);
        assertThat(d.GetPeriodo()).isEqualTo(YearMonth.of(2026, 6));
    }

    @Test
    void unaDonacionAnteriorALaDesnormalizacionSeCalculaAlVuelo() {
        DonacionRegistrada d = new DonacionRegistrada("ana", "alimentos", List.of("arroz", "fideos"), "Comedor", LocalDate.of(2026, 6, 5));
        ReflectionTestUtils.setField(d, "cantidadBienes", null);
        ReflectionTestUtils.setField(d, "periodo", null);

        assertThat(d.GetCantidadBienes()).isEqualTo(2);
        assertThat(d.GetPeriodo()).isEqualTo(YearMonth.of(2026, 6));
    }

    @Test
    void losContadoresDelDonanteSeActualizanAlRegistrar() {
        DonanteIncentivos donante = new DonanteIncentivos("ana");
        donante.RegistrarActividad(donacion(3, LocalDate.of(2026, 1, 10)));
        donante.RegistrarActividad(donacion(2, LocalDate.of(2026, 2, 10)));

        assertThat(ReflectionTestUtils.getField(donante, "totalDonaciones")).isEqualTo(2);
        assertThat(ReflectionTestUtils.getField(donante, "impactoAcumulado")).isEqualTo(5);
        assertThat(donante.CalcularTotalDonaciones()).isEqualTo(2);
        assertThat(donante.CalcularImpactoAcumulado()).isEqualTo(5);
    }

    @Test
    void unDonanteSinContadoresLosRecalculaYNoCuentaDeMasAlRegistrar() {
        DonanteIncentivos donante = new DonanteIncentivos("ana");
        donante.RegistrarActividad(donacion(3, LocalDate.of(2026, 1, 10)));
       
        // simula una fila cargada de la base antes de existir las columnas nuevas
        ReflectionTestUtils.setField(donante, "totalDonaciones", null);
        ReflectionTestUtils.setField(donante, "impactoAcumulado", null);

        donante.RegistrarActividad(donacion(2, LocalDate.of(2026, 2, 10)));

        assertThat(donante.CalcularTotalDonaciones()).isEqualTo(2);
        assertThat(donante.CalcularImpactoAcumulado()).isEqualTo(5);
    }

    private DonacionRegistrada donacion(int cantidad, LocalDate fecha) {
        return new DonacionRegistrada("ana", "alimentos", IntStream.range(0, cantidad).mapToObj(i -> "bien-" + i).toList(), "Comedor", fecha);
    }
}