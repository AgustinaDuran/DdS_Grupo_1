package org.donatrack.service;

import org.donatrack.model.Shipment;
import org.donatrack.model.ShipmentStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ShipmentService {

    private final Map<Long, Shipment> shipments = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    /**
     * Si ya hay un envío activo con la misma externalRef se devuelve ese en vez de crear otro:
     * así un reintento del broker (por ejemplo, tras un timeout en el que el envío sí llegó) no
     * genera envíos duplicados. Un envío FAILED no cuenta, porque la donación se puede replanificar.
     */
    public synchronized Shipment crear(String externalRef, String destinationAddress) {
        Optional<Shipment> activo = shipments.values().stream()
                .filter(s -> s.getExternalRef().equals(externalRef))
                .filter(s -> s.getStatus() != ShipmentStatus.FAILED)
                .findFirst();
        if (activo.isPresent()) {
            return activo.get();
        }
        Shipment shipment = new Shipment(secuencia.incrementAndGet(), externalRef, destinationAddress);
        shipments.put(shipment.getShipmentId(), shipment);
        return shipment;
    }

    public List<Shipment> listar() {
        return new ArrayList<>(shipments.values());
    }

    public Optional<Shipment> buscar(Long shipmentId) {
        return Optional.ofNullable(shipments.get(shipmentId));
    }
}
