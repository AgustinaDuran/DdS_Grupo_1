package org.donatrack.controller;

import java.util.Map;

import org.donatrack.controller.dto.PaginaDTO;
import org.donatrack.controller.dto.Asignaciones.DonacionPendienteDTO;
import org.donatrack.controller.dto.Asignaciones.RankingDonacionDTO;
import org.donatrack.service.AsignacionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO flujo 1: todos estos endpoints son solo para el rol ADMIN.
@RestController
@RequestMapping("/api/asignaciones")
@CrossOrigin(origins = "*")
public class AsignacionesController {

    private final AsignacionesService asignacionesService;

    public AsignacionesController(AsignacionesService asignacionesService) {
        this.asignacionesService = asignacionesService;
    }

    // http://localhost:8080/api/asignaciones/ejecuciones -> POST
    // Ejecuta a demanda el cálculo de rankings que normalmente corre de noche.
    @PostMapping("/ejecuciones")
    public ResponseEntity<Map<String, Integer>> ejecutarCalculo() {
        int procesadas = asignacionesService.ejecutarCalculo();
        return ResponseEntity.ok(Map.of("donacionesProcesadas", procesadas));
    }

    // http://localhost:8080/api/asignaciones/pendientes?page=0&size=20&subcategoriaId=3 -> GET
    @GetMapping("/pendientes")
    public ResponseEntity<PaginaDTO<DonacionPendienteDTO>> obtenerPendientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long subcategoriaId) {
        return ResponseEntity.ok(asignacionesService.obtenerPendientes(page, size, subcategoriaId));
    }

    // http://localhost:8080/api/asignaciones/pendientes/128 -> GET
    // 404 si la donación no existe o ya no está en depósito.
    @GetMapping("/pendientes/{donacionId}")
    public ResponseEntity<RankingDonacionDTO> obtenerRanking(@PathVariable Long donacionId) {
        return asignacionesService.obtenerRanking(donacionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
