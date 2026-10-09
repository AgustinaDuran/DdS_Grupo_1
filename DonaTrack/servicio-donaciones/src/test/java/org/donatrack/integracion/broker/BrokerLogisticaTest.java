package org.donatrack.integracion.broker;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.integracion.dto.DepositoRequest;
import org.donatrack.integracion.dto.EntregaRequest;
import org.donatrack.integracion.dto.EntregaResponse;
import org.junit.jupiter.api.Test;

/**
 * El broker registra con failover (el primer proveedor que responde) y lista consultando a
 * todos, porque por el failover una entrega puede haber quedado en cualquiera de ellos.
 */
class BrokerLogisticaTest {

    private final DepositoRequest request =
            new DepositoRequest(List.of(new EntregaRequest("1", "Calle Falsa 123")));

    @Test
    void registraEnElPrimerProveedorSiResponde() {
        ProveedorFalso propia = new ProveedorFalso("propia", false);
        ProveedorFalso alternativa = new ProveedorFalso("alternativa", false);

        new BrokerLogistica(List.of(propia, alternativa)).registrarEntregas(request);

        assertThat(propia.registrados).containsExactly(request);
        assertThat(alternativa.registrados).isEmpty();
    }

    @Test
    void siElPrimerProveedorFallaRegistraEnElSiguiente() {
        ProveedorFalso propia = new ProveedorFalso("propia", true);
        ProveedorFalso alternativa = new ProveedorFalso("alternativa", false);

        new BrokerLogistica(List.of(propia, alternativa)).registrarEntregas(request);

        assertThat(alternativa.registrados).containsExactly(request);
    }

    @Test
    void siFallanTodosLosProveedoresNoLanzaExcepcion() {
        BrokerLogistica broker = new BrokerLogistica(List.of(
                new ProveedorFalso("propia", true), new ProveedorFalso("alternativa", true)));

        broker.registrarEntregas(request);
    }

    @Test
    void listaLasEntregasDeTodosLosProveedores() {
        ProveedorFalso propia = new ProveedorFalso("propia", false);
        propia.entregas.add(entrega("1"));
        ProveedorFalso alternativa = new ProveedorFalso("alternativa", false);
        alternativa.entregas.add(entrega("2"));

        List<EntregaResponse> entregas = new BrokerLogistica(List.of(propia, alternativa)).listarEntregas();

        assertThat(entregas).extracting(EntregaResponse::getDonacionId).containsExactly("1", "2");
    }

    @Test
    void unProveedorCaidoNoImpideListarLasEntregasDeLosDemas() {
        ProveedorFalso propia = new ProveedorFalso("propia", true);
        ProveedorFalso alternativa = new ProveedorFalso("alternativa", false);
        alternativa.entregas.add(entrega("2"));

        List<EntregaResponse> entregas = new BrokerLogistica(List.of(propia, alternativa)).listarEntregas();

        assertThat(entregas).extracting(EntregaResponse::getDonacionId).containsExactly("2");
    }

    private EntregaResponse entrega(String donacionId) {
        EntregaResponse entrega = new EntregaResponse();
        entrega.setDonacionId(donacionId);
        return entrega;
    }

    private static class ProveedorFalso implements ProveedorLogistica {

        private final String nombre;
        private final boolean caido;
        private final List<DepositoRequest> registrados = new ArrayList<>();
        private final List<EntregaResponse> entregas = new ArrayList<>();

        ProveedorFalso(String nombre, boolean caido) {
            this.nombre = nombre;
            this.caido = caido;
        }

        @Override
        public void registrarEntregas(DepositoRequest request) {
            if (caido) {
                throw new IllegalStateException("Connection refused");
            }
            registrados.add(request);
        }

        @Override
        public List<EntregaResponse> listarEntregas() {
            if (caido) {
                throw new IllegalStateException("Connection refused");
            }
            return entregas;
        }

        @Override
        public String nombre() {
            return nombre;
        }
    }
}
