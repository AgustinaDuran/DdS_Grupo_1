package org.donatrack.controller;

import org.donatrack.controller.dto.*;
import org.donatrack.controller.exception.RecursoNoEncontradoException;
import org.donatrack.service.AnaliticaService;
import org.donatrack.service.RankingProgramadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incentivos")
@CrossOrigin(origins = "*")
public class IncentivosController {

    private final AnaliticaService analiticaService;
    private final RankingProgramadoService rankingProgramadoService;

    public IncentivosController(AnaliticaService analiticaService, RankingProgramadoService rankingProgramadoService) {
        this.analiticaService = analiticaService;
        this.rankingProgramadoService = rankingProgramadoService;
    }

    //http://localhost:8080/api/incentivos/donantes/{nombreUsuario}/perfil-analitico
    @GetMapping("/donantes/{nombreUsuario}/perfil-analitico")
    public ResponseEntity<PerfilAnaliticoDTO> ObtenerPerfil(@PathVariable String nombreUsuario) {
        return ResponseEntity.ok(this.analiticaService.ObtenerEstadisticasGenerales(nombreUsuario));
    }

    @PostMapping("/registrar-donacion")
    public ResponseEntity<String> RecibirNuevaDonacion(
        @RequestParam String nombreUsuario, 
        @RequestBody DonacionDTO nuevaDonacion) {
        
        analiticaService.RegistrarDonacionDeUsuario(nombreUsuario, nuevaDonacion);
        return ResponseEntity.ok("Donación procesada en Incentivos con éxito");
    }

    //http://localhost:8080/api/incentivos/donantes/{nombreUsuario}/misiones-progreso
    @GetMapping("/donantes/{nombreUsuario}/misiones-progreso")
    public ResponseEntity<List<MisionProgresoDTO>> ObtenerMisiones(@PathVariable String nombreUsuario) {
        return ResponseEntity.ok(this.analiticaService.ObtenerProgresoMisiones(nombreUsuario));
    }

    //http://localhost:8080/api/incentivos/donantes/{nombreUsuario}/vitrina
    @GetMapping("/donantes/{nombreUsuario}/vitrina")
    public ResponseEntity<List<InsigniaDTO>> ObtenerVitrina(@PathVariable String nombreUsuario) {
        return ResponseEntity.ok(this.analiticaService.ObtenerInsignias(nombreUsuario));
    }

    //http://localhost:8080/api/incentivos/ranking/destacados
    @GetMapping("/ranking/destacados")
    public ResponseEntity<PodioMensualDTO> ObtenerPodioDestacado(@RequestParam(required = false) String periodo) {
        YearMonth mesAConsultar = (periodo != null) ? YearMonth.parse(periodo) : YearMonth.now().minusMonths(1);
        return ResponseEntity.ok(this.analiticaService.ObtenerPodioDestacadoDelMes(mesAConsultar));
    }

    @PostMapping("/ranking/generar")
    public ResponseEntity<PodioMensualDTO> GenerarRankingManual(
            @RequestParam(required = false) String periodo) {
        YearMonth mesAProcesar = (periodo != null) ? YearMonth.parse(periodo) : YearMonth.now().minusMonths(1);
        rankingProgramadoService.GenerarRankingDe(mesAProcesar);
        return ResponseEntity.ok(analiticaService.ObtenerPodioDestacadoDelMes(mesAProcesar));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(RecursoNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not Found",
                "message", exception.getMessage()
        ));
    }

    @ExceptionHandler(java.time.format.DateTimeParseException.class)
    public ResponseEntity<Map<String, Object>> manejarPeriodoInvalido(java.time.format.DateTimeParseException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "status", HttpStatus.BAD_REQUEST.value(),
            "error", "Bad Request",
            "message", "El parámetro 'periodo' debe tener el formato yyyy-MM"
    ));
}
}