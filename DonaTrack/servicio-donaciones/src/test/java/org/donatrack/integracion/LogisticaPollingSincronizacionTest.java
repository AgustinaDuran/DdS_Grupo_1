package org.donatrack.integracion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.EstadoAsignacionRealizada;
import org.donatrack.dominio.donacion.TipoEstado;
import org.donatrack.integracion.broker.BrokerLogistica;
import org.donatrack.integracion.dto.EntregaResponse;
import org.donatrack.repository.DonacionesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * El polling tiene que persistir el estado que detecta en Logística: si sólo modifica la
 * donación en memoria, en el siguiente ciclo la vuelve a leer sin cambios y la deja para siempre
 * en ASIGNACION_REALIZADA.
 */
class LogisticaPollingSincronizacionTest {

    private final BrokerLogistica broker = mock(BrokerLogistica.class);
    private final DonacionesRepository donacionesRepository = mock(DonacionesRepository.class);
    private final LogisticaPollingScheduler scheduler = new LogisticaPollingScheduler(
            broker, donacionesRepository, mock(NotificacionesClient.class), mock(DestinatarioResolver.class),
            new TransactionTemplate(mock(PlatformTransactionManager.class)));

    private Donacion donacion;

    @BeforeEach
    void donacionAsignada() {
        donacion = new Donacion(null, new ArrayList<>());
        donacion.setEstadoDonacion(new EstadoAsignacionRealizada());
        when(donacionesRepository.findById(7L)).thenReturn(Optional.of(donacion));
    }

    @Test
    void guardaElNuevoEstadoDeLaDonacion() {
        when(broker.listarEntregas()).thenReturn(List.of(entrega("EN_TRASLADO", null)));

        scheduler.sincronizarEventosDeLogistica();

        assertThat(donacion.getEstadoDonacion().getEstado()).isEqualTo(TipoEstado.EN_TRASLADO);
        verify(donacionesRepository).save(donacion);
    }

    @Test
    void alEntregarseCopiaLaFotoDelComprobante() {
        when(broker.listarEntregas()).thenReturn(List.of(entrega("ENTREGADA", "https://fotos/entrega-7.jpg")));

        scheduler.sincronizarEventosDeLogistica();

        assertThat(donacion.getEstadoDonacion().getEstado()).isEqualTo(TipoEstado.ENTREGADA);
        assertThat(donacion.getFotoEntrega()).isEqualTo("https://fotos/entrega-7.jpg");
        verify(donacionesRepository).save(donacion);
    }

    private EntregaResponse entrega(String estado, String fotoUrl) {
        EntregaResponse entrega = new EntregaResponse();
        entrega.setId(1L);
        entrega.setDonacionId("7");
        entrega.setEstado(estado);
        entrega.setFotoComprobanteUrl(fotoUrl);
        return entrega;
    }
}
