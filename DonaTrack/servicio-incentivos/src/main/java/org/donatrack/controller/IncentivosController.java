package org.donatrack.controller;

import org.donatrack.controller.dto.*;
import org.donatrack.service.AnaliticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/incentivos")
@CrossOrigin(origins = "*")
public class IncentivosController {

    private final AnaliticaService analiticaService;

    public IncentivosController(AnaliticaService analiticaService) {
        this.analiticaService = analiticaService;
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
    public ResponseEntity<PodioMensualDTO> ObtenerPodioDestacado() {
        YearMonth mesActual = YearMonth.now(); 
        return ResponseEntity.ok(this.analiticaService.ObtenerPodioDestacadoDelMes(mesActual));
    }
}