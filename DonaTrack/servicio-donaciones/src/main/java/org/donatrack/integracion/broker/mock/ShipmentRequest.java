package org.donatrack.integracion.broker.mock;

/** Envío en el formato que recibe el proveedor de logística externo (POST /v1/shipments). */
public class ShipmentRequest {

    private String externalRef;
    private String destinationAddress;

    public ShipmentRequest() {
    }

    public ShipmentRequest(String externalRef, String destinationAddress) {
        this.externalRef = externalRef;
        this.destinationAddress = destinationAddress;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }
}
