package org.donatrack.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.donatrack.integracion.dto.EntregaResponse;
import org.junit.jupiter.api.Test;

/**
 * Una donación puede tener varias entregas en Logística (replanificación tras una entrega no
 * recibida, o filas duplicadas por un callback sin id). El polling debe quedarse con una sola
 * por donación, porque comparar todas contra el único estado local de la donación lo hace
 * notificar en loop en cada ciclo.
 */
class LogisticaPollingSchedulerTest {

    private final LogisticaPollingScheduler scheduler =
            new LogisticaPollingScheduler(null, null, null, null);

    @Test
    void seQuedaConLaEntregaMasRecienteDeCadaDonacion() {
        List<EntregaResponse> seleccionadas = scheduler.ultimaEntregaPorDonacion(List.of(
                entrega(1L, "1", "ENTREGADA"),
                entrega(5L, "1", "EN_TRASLADO"),
                entrega(9L, "1", "PENDIENTE")));

        assertThat(seleccionadas).hasSize(1);
        assertThat(seleccionadas.get(0).getId()).isEqualTo(9L);
    }

    @Test
    void noPierdeEntregasDeDonacionesDistintas() {
        List<EntregaResponse> seleccionadas = scheduler.ultimaEntregaPorDonacion(List.of(
                entrega(1L, "1", "EN_TRASLADO"),
                entrega(2L, "2", "ENTREGADA"),
                entrega(3L, "3", "PENDIENTE")));

        assertThat(seleccionadas)
                .extracting(EntregaResponse::getDonacionId)
                .containsExactly("1", "2", "3");
    }

    @Test
    void elOrdenDeLlegadaNoAlteraElResultado() {
        List<EntregaResponse> ascendente = scheduler.ultimaEntregaPorDonacion(List.of(
                entrega(4L, "7", "EN_TRASLADO"),
                entrega(8L, "7", "ENTREGADA")));
        List<EntregaResponse> descendente = scheduler.ultimaEntregaPorDonacion(List.of(
                entrega(8L, "7", "ENTREGADA"),
                entrega(4L, "7", "EN_TRASLADO")));

        assertThat(ascendente.get(0).getId()).isEqualTo(8L);
        assertThat(descendente.get(0).getId()).isEqualTo(8L);
    }

    @Test
    void ignoraEntregasSinDonacionAsociada() {
        List<EntregaResponse> seleccionadas = scheduler.ultimaEntregaPorDonacion(List.of(
                entrega(1L, null, "ENTREGADA"),
                entrega(2L, "4", "EN_TRASLADO")));

        assertThat(seleccionadas)
                .extracting(EntregaResponse::getDonacionId)
                .containsExactly("4");
    }

    @Test
    void toleraLaListaVacia() {
        assertThat(scheduler.ultimaEntregaPorDonacion(List.of())).isEmpty();
    }

    private EntregaResponse entrega(Long id, String donacionId, String estado) {
        EntregaResponse entrega = new EntregaResponse();
        entrega.setId(id);
        entrega.setDonacionId(donacionId);
        entrega.setEstado(estado);
        return entrega;
    }
}
