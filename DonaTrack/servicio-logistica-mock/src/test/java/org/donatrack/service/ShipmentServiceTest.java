package org.donatrack.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.donatrack.model.Shipment;
import org.donatrack.model.ShipmentStatus;
import org.junit.jupiter.api.Test;

class ShipmentServiceTest {

    private final ShipmentService service = new ShipmentService();

    @Test
    void unReintentoConLaMismaReferenciaNoDuplicaElEnvio() {
        Shipment primero = service.crear("1", "Calle Falsa 123");
        Shipment reintento = service.crear("1", "Calle Falsa 123");

        assertThat(reintento.getShipmentId()).isEqualTo(primero.getShipmentId());
        assertThat(service.listar()).hasSize(1);
    }

    @Test
    void unEnvioFallidoPermiteCrearOtroParaLaMismaReferencia() {
        Shipment fallido = service.crear("1", "Calle Falsa 123");
        fallido.actualizarEstado(ShipmentStatus.FAILED, null, "Nadie respondió");

        Shipment nuevo = service.crear("1", "Calle Falsa 123");

        assertThat(nuevo.getShipmentId()).isNotEqualTo(fallido.getShipmentId());
        assertThat(service.listar()).hasSize(2);
    }
}
