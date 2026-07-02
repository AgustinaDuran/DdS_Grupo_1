package org.donatrack.controller;

import org.donatrack.controller.dto.CamionDTO;
import org.donatrack.controller.dto.DepositoDTO;
import org.donatrack.controller.dto.UbicacionDTO;
import org.donatrack.model.Camion;
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

    //admin camiones
    @GetMapping("/camiones")
    public ResponseEntity<List<Camion>> listarCamiones() {
        return ResponseEntity.ok(logisticaService.listarCamiones());
    }
 
    @PostMapping("/camiones")
    public ResponseEntity<Camion> crearCamion(@RequestBody CamionDTO camionDTO) {
        return ResponseEntity.ok(logisticaService.crearCamion(camionDTO));
    }
 
    @PutMapping("/camiones/{patente}")
    public ResponseEntity<Camion> modificarCamion(@PathVariable String patente, @RequestBody CamionDTO camionDTO) {
        return ResponseEntity.ok(logisticaService.modificarCamion(patente, camionDTO));
    }
 
    @DeleteMapping("/camiones/{patente}")
    public ResponseEntity<String> darDeBajaCamion(@PathVariable String patente) {
        logisticaService.darDeBajaCamion(patente);
        return ResponseEntity.ok("Camión " + patente + " dado de baja correctamente.");
    }

    //seguimiento tiempo real
    @PutMapping("/camiones/{patente}/ubicacion")
    public ResponseEntity<String> actualizarUbicacion(@PathVariable String patente, @RequestBody UbicacionDTO ubicacionDTO) {
        logisticaService.actualizarUbicacionCamion(patente, ubicacionDTO.getLatitud(), ubicacionDTO.getLongitud());
        return ResponseEntity.ok("Ubicación del camión " + patente + " actualizada.");
    }

    //para otros servicios
    @GetMapping("/entregas/{id}")
    public ResponseEntity<Entrega> consultarEntrega(@PathVariable Long id) {
        return ResponseEntity.ok(logisticaService.ConsultarEntrega(id));
    }

    @GetMapping("/entregas")
    public ResponseEntity<List<Entrega>> listarEntregas(@RequestParam(required = false) EstadoEntrega estado) {
        return ResponseEntity.ok(logisticaService.listarEntregas(estado));
    }
 
    @GetMapping("/camiones/{patente}/ruta-activa")
    public ResponseEntity<RutaReparto> consultarRutaActiva(@PathVariable String patente) {
        return ResponseEntity.ok(logisticaService.ConsultarRutaActiva(patente));
    }

    @GetMapping("/rutas-activas")
    public ResponseEntity<List<RutaReparto>> listarRutasActivas() {
        return ResponseEntity.ok(logisticaService.listarRutasActivas());
    }
}