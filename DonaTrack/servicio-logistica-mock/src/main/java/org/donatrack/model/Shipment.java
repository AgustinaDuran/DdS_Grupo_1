package org.donatrack.model;

import java.time.LocalDateTime;

/** Envío del proveedor externo. Se devuelve tal cual en las respuestas de la API. */
public class Shipment {

    private Long shipmentId;
    private String externalRef;
    private String destinationAddress;
    private ShipmentStatus status;
    private String truckPlate;
    private LocalDateTime deliveredAt;
    private String failureReason;

    public Shipment(Long shipmentId, String externalRef, String destinationAddress) {
        this.shipmentId = shipmentId;
        this.externalRef = externalRef;
        this.destinationAddress = destinationAddress;
        this.status = ShipmentStatus.CREATED;
    }

    public void actualizarEstado(ShipmentStatus nuevoEstado, String truckPlate, String failureReason) {
        this.status = nuevoEstado;
        if (truckPlate != null) {
            this.truckPlate = truckPlate;
        }
        if (nuevoEstado == ShipmentStatus.DELIVERED) {
            this.deliveredAt = LocalDateTime.now();
        }
        if (nuevoEstado == ShipmentStatus.FAILED) {
            this.failureReason = failureReason;
        }
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public String getTruckPlate() {
        return truckPlate;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
