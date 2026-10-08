package org.donatrack.controller;

import org.donatrack.controller.dto.ShipmentRequest;
import org.donatrack.controller.dto.StatusUpdateRequest;
import org.donatrack.model.Shipment;
import org.donatrack.service.ShipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/shipments")
public class ShipmentsController {

    private final ShipmentService shipmentService;

    public ShipmentsController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    public ResponseEntity<Shipment> crear(@RequestBody ShipmentRequest request) {
        if (request.getExternalRef() == null || request.getDestinationAddress() == null) {
            return ResponseEntity.badRequest().build();
        }
        Shipment shipment = shipmentService.crear(request.getExternalRef(), request.getDestinationAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(shipment);
    }

    @GetMapping
    public List<Shipment> listar() {
        return shipmentService.listar();
    }

    @GetMapping("/{shipmentId}")
    public ResponseEntity<Shipment> obtener(@PathVariable Long shipmentId) {
        return ResponseEntity.of(shipmentService.buscar(shipmentId));
    }

    /** Simula el avance del envío (salida del camión, entrega o fallo) para poder probar el polling. */
    @PatchMapping("/{shipmentId}/status")
    public ResponseEntity<Shipment> actualizarEstado(@PathVariable Long shipmentId,
                                                     @RequestBody StatusUpdateRequest request) {
        if (request.getStatus() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.of(shipmentService.buscar(shipmentId).map(shipment -> {
            shipment.actualizarEstado(request.getStatus(), request.getTruckPlate(), request.getFailureReason());
            return shipment;
        }));
    }
}
