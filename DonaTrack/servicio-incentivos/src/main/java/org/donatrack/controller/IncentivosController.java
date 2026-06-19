package org.donatrack.controller;
import java.time.YearMonth;
import org.donatrack.controller.dto.*;

import org.donatrack.service.AnaliticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PerfilAnaliticoDTO> obtenerPerfil(@PathVariable String nombreUsuario) {
        return ResponseEntity.ok(this.analiticaService.obtenerEstadisticasGenerales(nombreUsuario));
    }

    @PostMapping("/registrar-donacion")
    public ResponseEntity<String> recibirNuevaDonacion(
        @RequestParam String nombreUsuario, 
        @RequestBody Donacion nuevaDonacion) {
        
        analiticaService.registrarDonacionDeUsuario(nombreUsuario, nuevaDonacion);
        
        return ResponseEntity.ok("Donación procesada en Incentivos con éxito");
    }

    //http://localhost:8080/api/incentivos/donantes/{nombreUsuario}/misiones-progreso
    @GetMapping("/donantes/{nombreUsuario}/misiones-progreso")
    public ResponseEntity<List<MisionProgresoDTO>> obtenerMisiones(@PathVariable String nombreUsuario) {
        return ResponseEntity.ok(this.analiticaService.obtenerProgresoMisiones(nombreUsuario));
    }

    //http://localhost:8080/api/incentivos/donantes/{nombreUsuario}/vitrina
    @GetMapping("/donantes/{nombreUsuario}/vitrina")
    public ResponseEntity<List<InsigniaDTO>> obtenerVitrina(@PathVariable String nombreUsuario) {
        return ResponseEntity.ok(this.analiticaService.obtenerInsignias(nombreUsuario));
    }
    //http://localhost:8080/api/incentivos/ranking/destacados
    @GetMapping("/ranking/destacados")
    public ResponseEntity<PodioMensualDTO> obtenerPodioDestacado() {
        YearMonth mesActual = YearMonth.now(); 
        return ResponseEntity.ok(this.analiticaService.obtenerPodioDestacadoDelMes(mesActual));
}
}