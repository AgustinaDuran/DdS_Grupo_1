package org.donatrack;

import org.donatrack.model.Donacion;
import org.donatrack.model.DonanteIncentivos;
import org.donatrack.model.Insignia;
import org.donatrack.model.MisionCompletitud;
import org.donatrack.model.MisionDonacionesExitosas;
import org.donatrack.model.MisionHabilDonador;
import org.donatrack.model.MisionRacha;
import org.donatrack.model.NivelInsignia;
import org.donatrack.model.Organizacion;
import org.donatrack.model.Subcategoria;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica la condición de cumplimiento y la distancia restante de cada
 * tipo de misión, por encima y por debajo del umbral.
 */
class MisionesTest {

    private final Insignia insignia = new Insignia("Test", "img.png", NivelInsignia.BRONCE);

    private Donacion donacion(LocalDate fecha, int cantidad, Subcategoria sub, Organizacion org) {
        return new Donacion(sub, org, fecha, cantidad);
    }

    @Test
    void misionRachaSeCumpleConMesesConsecutivos() {
        DonanteIncentivos donante = new DonanteIncentivos("ana", "Colaborador");
        Subcategoria s = new Subcategoria("Arroz");
        Organizacion o = new Organizacion("Comedor");
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 1), 1, s, o));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 2, 1), 1, s, o));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 3, 1), 1, s, o));

        MisionRacha mision = new MisionRacha("Racha de 3 meses", insignia, 3);

        assertTrue(mision.estaCumplidaPor(donante));
        assertEquals(0, mision.getDistanciaRestante(donante));
    }

    @Test
    void misionRachaNoSeCumpleYReportaDistancia() {
        DonanteIncentivos donante = new DonanteIncentivos("ana", "Colaborador");
        Subcategoria s = new Subcategoria("Arroz");
        Organizacion o = new Organizacion("Comedor");
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 1), 1, s, o));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 2, 1), 1, s, o));

        MisionRacha mision = new MisionRacha("Racha de 3 meses", insignia, 3);

        assertFalse(mision.estaCumplidaPor(donante));
        assertEquals(1, mision.getDistanciaRestante(donante)); // racha 2, faltan 1
    }

    @Test
    void misionCompletitudSeCumpleConSubcategoriasDistintas() {
        DonanteIncentivos donante = new DonanteIncentivos("ana", "Colaborador");
        Organizacion o = new Organizacion("Comedor");
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 1), 1, new Subcategoria("Arroz"), o));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 2), 1, new Subcategoria("Fideos"), o));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 3), 1, new Subcategoria("Ropa"), o));

        MisionCompletitud mision = new MisionCompletitud("3 categorías", insignia, 3);

        assertTrue(mision.estaCumplidaPor(donante));
        assertEquals(0, mision.getDistanciaRestante(donante));
    }

    @Test
    void misionHabilDonadorSeCumpleSuperandoCantidadDeBienes() {
        DonanteIncentivos donante = new DonanteIncentivos("ana", "Colaborador");
        Subcategoria s = new Subcategoria("Arroz");
        Organizacion o = new Organizacion("Comedor");
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 1), 10, s, o));

        MisionHabilDonador mision = new MisionHabilDonador("Superar 2 bienes", insignia, 2);

        assertTrue(mision.estaCumplidaPor(donante));
    }

    @Test
    void misionDonacionesExitosasSeCumpleAyudandoVariasOrganizaciones() {
        DonanteIncentivos donante = new DonanteIncentivos("ana", "Colaborador");
        Subcategoria s = new Subcategoria("Arroz");
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 1), 1, s, new Organizacion("Comedor")));
        donante.getDonaciones().add(donacion(LocalDate.of(2026, 1, 2), 1, s, new Organizacion("Escuela")));

        MisionDonacionesExitosas mision = new MisionDonacionesExitosas("2 organizaciones", insignia, 2);

        assertTrue(mision.estaCumplidaPor(donante));
        assertEquals(0, mision.getDistanciaRestante(donante));
    }
}
