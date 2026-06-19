package org.donatrack;

import org.donatrack.model.Donacion;
import org.donatrack.model.DonanteIncentivos;
import org.donatrack.model.Organizacion;
import org.donatrack.model.Subcategoria;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifica los cálculos de analítica del donante: racha de meses,
 * subcategorías distintas, organizaciones ayudadas e impacto acumulado.
 */
class AnaliticaTest {

    private final Subcategoria arroz = new Subcategoria("Arroz");
    private final Subcategoria fideos = new Subcategoria("Fideos");
    private final Subcategoria ropa = new Subcategoria("Ropa");
    private final Organizacion comedor = new Organizacion("Comedor Sonrisas");
    private final Organizacion escuela = new Organizacion("Escuela Rural 10");

    private Donacion donacion(LocalDate fecha, int cantidad, Subcategoria sub, Organizacion org) {
        return new Donacion(sub, org, fecha, cantidad);
    }

    @Test
    void rachaCuentaMesesConsecutivos() {
        DonanteIncentivos donante = new DonanteIncentivos("ana", "Colaborador");
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 10), 1, arroz, comedor));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 2, 10), 1, arroz, comedor));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 3, 10), 1, arroz, comedor));

        assertEquals(3, donante.CalcularRachaActual());
    }

    @Test
    void rachaSeReiniciaAnteUnHueco() {
        DonanteIncentivos donante = new DonanteIncentivos("ana", "Colaborador");
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 10), 1, arroz, comedor));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 2, 10), 1, arroz, comedor));
        // Salto de marzo: el mes de abril reinicia la racha a 1.
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 4, 10), 1, arroz, comedor));

        assertEquals(1, donante.CalcularRachaActual());
    }

    @Test
    void donacionesDistintasCuentaSubcategoriasUnicas() {
        DonanteIncentivos donante = new DonanteIncentivos("ana", "Colaborador");
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 10), 1, arroz, comedor));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 11), 1, arroz, comedor)); // repetida
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 12), 1, fideos, comedor));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 13), 1, ropa, comedor));

        assertEquals(3, donante.CalcularDonacionesDistintas());
    }

    @Test
    void organizacionesAyudadasCuentaEntidadesUnicas() {
        DonanteIncentivos donante = new DonanteIncentivos("ana", "Colaborador");
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 10), 1, arroz, comedor));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 11), 1, fideos, comedor)); // misma org
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 12), 1, ropa, escuela));

        assertEquals(2, donante.CalcularOrganizacionesAyudadas());
    }

    @Test
    void impactoAcumuladoSumaCantidades() {
        DonanteIncentivos donante = new DonanteIncentivos("ana", "Colaborador");
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 10), 5, arroz, comedor));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 11), 3, fideos, escuela));

        assertEquals(8, donante.CalcularImpactoAcumulado());
        assertEquals(8, donante.calcularImpactoAcumulado()); // alias usado por los servicios
    }
}
