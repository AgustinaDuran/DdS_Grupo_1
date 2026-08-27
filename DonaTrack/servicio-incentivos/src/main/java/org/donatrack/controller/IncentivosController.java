package org.donatrack.controller;

import org.donatrack.controller.dto.*;
import org.donatrack.controller.exception.PeriodoInvalidoException;
import org.donatrack.controller.exception.RankingNoProcesadoException;
import org.donatrack.service.AnaliticaService;
import org.donatrack.service.RankingProgramadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incentivos")
@CrossOrigin(origins = "*")
public class IncentivosController {

    private final AnaliticaService analiticaService;
    private final RankingProgramadoService rankingProgramadoService;

    public IncentivosController(AnaliticaService analiticaService,
                                RankingProgramadoService rankingProgramadoService) {
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
    /**
     * Podio del período indicado (formato YYYY-MM). Sin parámetro usa el mes en curso, que es
     * el que genera la ejecución a demanda de abajo; el proceso programado del día 1 persiste
     * el mes que acaba de cerrar, así que para consultarlo hay que pasarlo explícitamente.
     */
    @GetMapping("/ranking/destacados")
    public ResponseEntity<PodioMensualDTO> ObtenerPodioDestacado(
            @RequestParam(required = false) String periodo) {
        return ResponseEntity.ok(this.analiticaService.ObtenerPodioDestacadoDelMes(parsearPeriodo(periodo)));
    }

    /** Ejecución a demanda del ranking, para no depender del cron del día 1 a medianoche. */
    @PostMapping("/ranking/procesar")
    public ResponseEntity<PodioMensualDTO> ProcesarRanking(
            @RequestParam(required = false) String periodo) {
        YearMonth mes = parsearPeriodo(periodo);
        rankingProgramadoService.procesarPeriodo(mes);
        return ResponseEntity.ok(this.analiticaService.ObtenerPodioDestacadoDelMes(mes));
    }

    private YearMonth parsearPeriodo(String periodo) {
        if (periodo == null || periodo.isBlank()) {
            return YearMonth.now();
        }
        try {
            return YearMonth.parse(periodo.trim());
        } catch (DateTimeParseException e) {
            throw new PeriodoInvalidoException(periodo);
        }
    }

    @ExceptionHandler(RankingNoProcesadoException.class)
    public ResponseEntity<Map<String, Object>> manejarRankingNoProcesado(RankingNoProcesadoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not Found",
                "message", exception.getMessage()
        ));
    }
}