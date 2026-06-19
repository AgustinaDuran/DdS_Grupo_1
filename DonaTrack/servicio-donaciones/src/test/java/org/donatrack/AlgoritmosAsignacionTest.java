package org.donatrack;

import org.donatrack.dominio.algoritmo.CompatibilidadSemantica;
import org.donatrack.dominio.algoritmo.CoordinadorAsignacion;
import org.donatrack.dominio.algoritmo.PrioridadSubatendidos;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.EstadoEntregada;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.entidadBeneficiaria.TipoEntidadBeneficiaria;
import org.donatrack.dominio.necesidades.NecesidadExtraordinaria;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifica los algoritmos de asignación (matchmaking) y la consolidación
 * de coincidencias del coordinador.
 */
class AlgoritmosAsignacionTest {

    private EntidadBeneficiaria entidad(String dir) {
        return new EntidadBeneficiaria(TipoEntidadBeneficiaria.COMEDOR, dir);
    }

    @Test
    void compatibilidadFiltraSinNecesidadYOrdenaPorPuntaje() {
        Subcategoria arroz = new Subcategoria("Arroz", null);

        EntidadBeneficiaria sinNecesidad = entidad("A");
        EntidadBeneficiaria pocaNecesidad = entidad("B");
        EntidadBeneficiaria muchaNecesidad = entidad("C");

        pocaNecesidad.registrarNecesidad(new NecesidadExtraordinaria(pocaNecesidad, arroz, "poco", 5, true, "m"));
        muchaNecesidad.registrarNecesidad(new NecesidadExtraordinaria(muchaNecesidad, arroz, "mucho", 50, true, "m"));

        Donacion donacion = mock(Donacion.class);
        when(donacion.getSubcategoria()).thenReturn(arroz);

        List<EntidadBeneficiaria> ranking = new CompatibilidadSemantica()
                .generarRanking(donacion, List.of(sinNecesidad, pocaNecesidad, muchaNecesidad));

        // La entidad sin necesidad compatible se filtra; mayor puntaje primero.
        assertEquals(List.of(muchaNecesidad, pocaNecesidad), ranking);
    }

    @Test
    void compatibilidadIgnoraNecesidadInactiva() {
        Subcategoria arroz = new Subcategoria("Arroz", null);
        EntidadBeneficiaria entidad = entidad("A");
        entidad.registrarNecesidad(new NecesidadExtraordinaria(entidad, arroz, "x", 5, false, "m"));

        Donacion donacion = mock(Donacion.class);
        when(donacion.getSubcategoria()).thenReturn(arroz);

        List<EntidadBeneficiaria> ranking = new CompatibilidadSemantica()
                .generarRanking(donacion, List.of(entidad));

        assertTrue(ranking.isEmpty());
    }

    @Test
    void prioridadSubatendidosOrdenaPorMenosEntregasRecientes() {
        EntidadBeneficiaria pocas = entidad("pocas");
        EntidadBeneficiaria muchas = entidad("muchas");

        muchas.agregarDonacionRecibida(donacionEntregadaReciente());
        muchas.agregarDonacionRecibida(donacionEntregadaReciente());
        pocas.agregarDonacionRecibida(donacionEntregadaReciente());

        Donacion cualquiera = mock(Donacion.class);
        List<EntidadBeneficiaria> ranking = new PrioridadSubatendidos()
                .generarRanking(cualquiera, List.of(muchas, pocas));

        // Prioriza a quien recibió menos donaciones en el trimestre.
        assertEquals(List.of(pocas, muchas), ranking);
    }

    private Donacion donacionEntregadaReciente() {
        EstadoEntregada entregada = new EstadoEntregada();
        entregada.setDate();
        Donacion d = mock(Donacion.class);
        when(d.getEstadoDonacion()).thenReturn(entregada);
        return d;
    }

    @Test
    void interseccionDevuelveSoloEntidadesPresentesEnTodosLosRankings() {
        EntidadBeneficiaria a = entidad("a");
        EntidadBeneficiaria b = entidad("b");
        EntidadBeneficiaria c = entidad("c");

        CoordinadorAsignacion coordinador = new CoordinadorAsignacion(List.of());
        List<EntidadBeneficiaria> interseccion = coordinador.interseccion(
                List.of(List.of(a, b), List.of(b, c)));

        assertEquals(List.of(b), interseccion);
    }
}
