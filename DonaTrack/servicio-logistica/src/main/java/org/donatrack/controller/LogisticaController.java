package org.donatrack.controller;

import org.donatrack.model.Entrega;
import org.donatrack.model.EstadoEntrega;
import org.donatrack.model.RutaReparto;
import org.donatrack.service.LogisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logistica")
public class LogisticaController {

    @Autowired
    private LogisticaService logisticaService;

    @PostMapping("/entregas")
    public ResponseEntity<String> recibirNuevasEntregas(@RequestBody DepositoDTO depositoDTO) {
        logisticaService.registrarNuevasEntregas(depositoDTO.getEntregas());
        return ResponseEntity.ok("Entregas registradas correctamente en Logística.");
    }

    //armado lotes
    @PostMapping("/planificar")
    public ResponseEntity<String> planificarRutas() {
        String resultado = logisticaService.planificarRutasDelDia();
        return ResponseEntity.ok(resultado);
    }

    // Webhook/Callback público para que el servicio externo devuelva el diseño de mapas óptimo
    @PostMapping("/callback-planificador")
    public ResponseEntity<String> recibirCallbackPlanificador(@RequestBody List<RutaReparto> rutas) {
        logisticaService.procesarCallbackPlanificador(rutas);
        return ResponseEntity.ok("Rutas procesadas y asignadas exitosamente.");
    }

    @PutMapping("/camiones/{patente}/iniciar-ruta")
    public ResponseEntity<String> iniciarRuta(@PathVariable String patente) {
        logisticaService.iniciarRuta(patente);
        return ResponseEntity.ok("Ruta iniciada. Entregas en estado EN_TRASLADO.");
    }

    @PutMapping("/entregas/{id}/estado")
    public ResponseEntity<String> cambiarEstadoEntrega(
            @PathVariable Long id, 
            @RequestParam EstadoEntrega nuevoEstado,
            @RequestParam(required = false) String fotoUrl,
            @RequestParam(required = false) String motivo) {
        
        logisticaService.actualizarEstadoEntrega(id, nuevoEstado, fotoUrl, motivo);
        return ResponseEntity.ok("Estado de la entrega " + id + " actualizado a " + nuevoEstado);
    }

    //para otros servicios
    @GetMapping("/entregas/{id}")
    public ResponseEntity<Entrega> consultarEntrega(@PathVariable Long id) {
        return ResponseEntity.ok(logisticaService.ConsultarEntrega(id));
    }

    @GetMapping("/camiones/{patente}/ruta-activa")
    public ResponseEntity<RutaReparto> consultarRutaActiva(@PathVariable String patente) {
        return ResponseEntity.ok(logisticaService.ConsultarRutaActiva(patente));
    }
}