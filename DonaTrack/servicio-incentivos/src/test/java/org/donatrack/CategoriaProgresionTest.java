package org.donatrack;

import org.donatrack.model.Donacion;
import org.donatrack.model.DonanteIncentivos;
import org.donatrack.model.Organizacion;
import org.donatrack.model.Sostenedor;
import org.donatrack.model.Subcategoria;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica que al completar todas las misiones de Colaborador (racha de 3
 * meses, 3 subcategorías distintas, impacto >= 2 y 2 organizaciones) el
 * donante asciende automáticamente a la categoría Sostenedor.
 */
class CategoriaProgresionTest {

    @Test
    void ascendeASostenedorAlCompletarTodasLasMisionesDeColaborador() {
        DonanteIncentivos donante = new DonanteIncentivos("ana", "Colaborador");

        Subcategoria arroz = new Subcategoria("Arroz");
        Subcategoria fideos = new Subcategoria("Fideos");
        Subcategoria ropa = new Subcategoria("Ropa");
        Organizacion comedor = new Organizacion("Comedor");
        Organizacion escuela = new Organizacion("Escuela");

        donante.registrarActividad(new Donacion(arroz, comedor, LocalDate.of(2026, 1, 15), 1));
        assertEquals("Colaborador", donante.getCategoriaActual().getNombre());

        donante.registrarActividad(new Donacion(fideos, escuela, LocalDate.of(2026, 2, 15), 1));
        assertEquals("Colaborador", donante.getCategoriaActual().getNombre());

        // Esta tercera donación completa simultáneamente las 4 misiones de Colaborador.
        donante.registrarActividad(new Donacion(ropa, comedor, LocalDate.of(2026, 3, 15), 1));

        assertInstanceOf(Sostenedor.class, donante.getCategoriaActual());
        assertEquals("Sostenedor", donante.getNombreCategoriaActual());
        assertTrue(donante.getCategoriaActual().getMisionesDelNivel().size() > 0);
    }
}
