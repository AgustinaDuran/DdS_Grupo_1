package org.donatrack;

import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.EstadoAsignacionRealizada;
import org.donatrack.dominio.donacion.EstadoEnDeposito;
import org.donatrack.dominio.donacion.EstadoEnTraslado;
import org.donatrack.dominio.donacion.EstadoEntregaFallida;
import org.donatrack.dominio.donacion.EstadoEntregada;
import org.donatrack.dominio.donacion.EstadoListaParaEntregar;
import org.donatrack.dominio.donacion.EstadoVencido;
import org.donatrack.dominio.donacion.TipoEstado;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.entidadBeneficiaria.TipoEntidadBeneficiaria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Verifica la máquina de estados de una donación (patrón State):
 * transiciones válidas, transiciones inválidas que deben fallar,
 * y el registro del historial de estados para trazabilidad/auditoría.
 */
class DonacionEstadosTest {

    private Donacion donacion;
    private EntidadBeneficiaria entidad;

    @BeforeEach
    void setUp() {
        // ItemBien no tiene constructor público; alcanza con un mock para los tests de estado.
        Subcategoria arroz = new Subcategoria("Arroz", null);
        ItemBien item = mock(ItemBien.class);
        donacion = new Donacion(null, item, arroz);
        entidad = new EntidadBeneficiaria(TipoEntidadBeneficiaria.COMEDOR, "Calle Falsa 123");
    }

    @Test
    void nuevaDonacionArrancaEnDeposito() {
        assertTrue(donacion.getEstadoDonacion() instanceof EstadoEnDeposito);
        assertEquals(TipoEstado.EN_DEPOSITO, donacion.getEstadoDonacion().getEstado());
    }

    @Test
    void flujoCompletoHastaEntregada() {
        donacion.asignar(entidad);
        assertTrue(donacion.getEstadoDonacion() instanceof EstadoAsignacionRealizada);
        assertSame(entidad, donacion.getEntidadAEntregar());

        donacion.planificarRuta();
        assertTrue(donacion.getEstadoDonacion() instanceof EstadoListaParaEntregar);

        donacion.iniciarTraslado();
        assertTrue(donacion.getEstadoDonacion() instanceof EstadoEnTraslado);
        assertTrue(donacion.getEstadoDonacion().entregaActiva());

        donacion.confirmarEntrega();
        // El estado EstadoEntregada(entidad) no setea el enum interno, se valida por tipo.
        assertTrue(donacion.getEstadoDonacion() instanceof EstadoEntregada);
        assertTrue(entidad.getDonacionesRecibidas().contains(donacion));

        // Cada transición registra un estado en el historial: asignar, planificar, iniciar, confirmar.
        assertEquals(4, donacion.getHistorialEstados().size());
    }

    @Test
    void confirmarEntregaDesdeDepositoLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> donacion.confirmarEntrega());
    }

    @Test
    void planificarRutaDesdeDepositoLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> donacion.planificarRuta());
    }

    @Test
    void marcarComoVencidaDesdeDeposito() {
        donacion.marcarComoVencida();
        assertTrue(donacion.getEstadoDonacion() instanceof EstadoVencido);
        assertEquals(TipoEstado.VENCIDA, donacion.getEstadoDonacion().getEstado());
    }

    @Test
    void entregaFallidaVuelveADeposito() {
        donacion.asignar(entidad);
        donacion.planificarRuta();
        donacion.iniciarTraslado();

        donacion.registrarEntregaFallida("Tocamos timbre pero nadie respondió");
        assertTrue(donacion.getEstadoDonacion() instanceof EstadoEntregaFallida);

        donacion.volverADeposito();
        assertTrue(donacion.getEstadoDonacion() instanceof EstadoEnDeposito);
    }
}
