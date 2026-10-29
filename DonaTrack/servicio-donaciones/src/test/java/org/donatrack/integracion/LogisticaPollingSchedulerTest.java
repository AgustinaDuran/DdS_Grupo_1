package org.donatrack.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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
            new LogisticaPollingScheduler(null, null, null, null, null);

    @Test
    void seQuedaConLaEntregaMasRecienteDeCadaDonacion() {
        List<EntregaResponse> seleccionadas = scheduler.ultimaEntregaPorDonacion(List.of(
                entrega("1", "1", "ENTREGADA"),
                entrega("5", "1", "EN_TRASLADO"),
                entrega("9", "1", "PENDIENTE")));

        assertThat(seleccionadas).hasSize(1);
        assertThat(seleccionadas.get(0).getId()).isEqualTo("9");
    }

    @Test
    void noPierdeEntregasDeDonacionesDistintas() {
        List<EntregaResponse> seleccionadas = scheduler.ultimaEntregaPorDonacion(List.of(
                entrega("1", "1", "EN_TRASLADO"),
                entrega("2", "2", "ENTREGADA"),
                entrega("3", "3", "PENDIENTE")));

        assertThat(seleccionadas)
                .extracting(EntregaResponse::getDonacionId)
                .containsExactly("1", "2", "3");
    }

    @Test
    void elOrdenDeLlegadaNoAlteraElResultado() {
        List<EntregaResponse> ascendente = scheduler.ultimaEntregaPorDonacion(List.of(
                entrega("4", "7", "EN_TRASLADO"),
                entrega("8", "7", "ENTREGADA")));
        List<EntregaResponse> descendente = scheduler.ultimaEntregaPorDonacion(List.of(
                entrega("8", "7", "ENTREGADA"),
                entrega("4", "7", "EN_TRASLADO")));

        assertThat(ascendente.get(0).getId()).isEqualTo("8");
        assertThat(descendente.get(0).getId()).isEqualTo("8");
    }

    @Test
    void conObjectIdsDeMongoUsaLaFechaDeCreacion() {
        EntregaResponse vieja = entrega("6720f0a1c3b2a10001000002", "3", "NO_RECIBIDA");
        vieja.setFechaCreacion(LocalDateTime.of(2026, 10, 1, 10, 0));
        EntregaResponse nueva = entrega("6720f0a1c3b2a10001000001", "3", "PENDIENTE");
        nueva.setFechaCreacion(LocalDateTime.of(2026, 10, 2, 10, 0));

        List<EntregaResponse> seleccionadas = scheduler.ultimaEntregaPorDonacion(List.of(vieja, nueva));

        assertThat(seleccionadas.get(0).getId()).isEqualTo("6720f0a1c3b2a10001000001");
    }

    @Test
    void sinFechaComparaLosObjectIds() {
        List<EntregaResponse> seleccionadas = scheduler.ultimaEntregaPorDonacion(List.of(
                entrega("6720f0a1c3b2a10001000009", "3", "EN_TRASLADO"),
                entrega("6720f0a1c3b2a1000100000a", "3", "PENDIENTE")));

        assertThat(seleccionadas.get(0).getId()).isEqualTo("6720f0a1c3b2a1000100000a");
    }

    @Test
    void ignoraEntregasSinDonacionAsociada() {
        List<EntregaResponse> seleccionadas = scheduler.ultimaEntregaPorDonacion(List.of(
                entrega("1", null, "ENTREGADA"),
                entrega("2", "4", "EN_TRASLADO")));

        assertThat(seleccionadas)
                .extracting(EntregaResponse::getDonacionId)
                .containsExactly("4");
    }

    @Test
    void toleraLaListaVacia() {
        assertThat(scheduler.ultimaEntregaPorDonacion(List.of())).isEmpty();
    }

    private EntregaResponse entrega(String id, String donacionId, String estado) {
        EntregaResponse entrega = new EntregaResponse();
        entrega.setId(id);
        entrega.setDonacionId(donacionId);
        entrega.setEstado(estado);
        return entrega;
    }
}
