package org.donatrack.controller;

import java.util.Map;

import org.donatrack.controller.dto.PaginaDTO;
import org.donatrack.controller.dto.Asignaciones.DonacionPendienteDTO;
import org.donatrack.controller.dto.Asignaciones.RankingDonacionDTO;
import org.donatrack.service.AsignacionesService;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO flujo 1: todos estos endpoints son solo para el rol ADMIN.
@Tag(name = "Asignaciones", description = "Ranking de entidades sugeridas para cada donación en depósito")
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
    @Operation(summary = "Calcular los rankings a demanda",
            description = "Ejecuta los algoritmos de asignación sobre todas las donaciones en depósito y guarda el ranking de cada una. Normalmente corre de noche.")
    @PostMapping("/ejecuciones")
    public ResponseEntity<Map<String, Integer>> ejecutarCalculo() {
        int procesadas = asignacionesService.ejecutarCalculo();
        return ResponseEntity.ok(Map.of("donacionesProcesadas", procesadas));
    }

    // http://localhost:8080/api/asignaciones/pendientes?page=0&size=20&subcategoriaId=3 -> GET
    @Operation(summary = "Listar donaciones pendientes de asignación",
            description = "Donaciones en depósito, paginadas y ordenadas por fecha de ingreso (la más antigua primero), con un resumen de su ranking. ranking.estado = PENDIENTE si todavía no se calculó.")
    @GetMapping("/pendientes")
    public ResponseEntity<PaginaDTO<DonacionPendienteDTO>> obtenerPendientes(
            @Parameter(description = "Número de página, desde 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página (máximo 50)") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Filtra por subcategoría") @RequestParam(required = false) Long subcategoriaId) {
        return ResponseEntity.ok(asignacionesService.obtenerPendientes(page, size, subcategoriaId));
    }

    // http://localhost:8080/api/asignaciones/pendientes/128 -> GET
    // 404 si la donación no existe o ya no está en depósito.
    @Operation(summary = "Ver el ranking completo de una donación",
            description = "Si hay coincidencias entre los algoritmos, devuelve solo esa lista; si no, una lista por algoritmo.")
    @ApiResponse(responseCode = "200", description = "Ranking de la donación")
    @ApiResponse(responseCode = "404", description = "La donación no existe o ya no está en depósito")
    @GetMapping("/pendientes/{donacionId}")
    public ResponseEntity<RankingDonacionDTO> obtenerRanking(@PathVariable Long donacionId) {
        return asignacionesService.obtenerRanking(donacionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
