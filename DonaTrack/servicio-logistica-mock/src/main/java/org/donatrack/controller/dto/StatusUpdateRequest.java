package org.donatrack.controller.dto;

import org.donatrack.model.ShipmentStatus;

public class StatusUpdateRequest {

    private ShipmentStatus status;
    private String truckPlate;
    private String failureReason;

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public String getTruckPlate() {
        return truckPlate;
    }

    public void setTruckPlate(String truckPlate) {
        this.truckPlate = truckPlate;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
