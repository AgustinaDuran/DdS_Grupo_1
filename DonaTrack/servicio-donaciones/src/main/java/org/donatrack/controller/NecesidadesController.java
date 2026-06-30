package org.donatrack.controller;
import org.donatrack.service.NecesidadesService;
import org.donatrack.service.DonacionesService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.YearMonth;
import java.util.List;


@RestController
@RequestMapping("/api/necesidades")
@CrossOrigin(origins = "*")
public class NecesidadesController {

    private final NecesidadesService necesidadesService;

    public NecesidadesController(NecesidadesService necesidadesService) {
        this.necesidadesService = necesidadesService;
    }

/*     //http://localhost:8080/api/donaciones
    @GetMapping("/")
    public ResponseEntity<PerfilAnaliticoDTO> obtenerPerfil(@PathVariable String nombreUsuario) {
        return ResponseEntity.ok(this.donacionesService.obtenerDonaciones());
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
        return ResponseEntity.ok(this.analiticaService.obtenerVitrinaInsignias(nombreUsuario));
    }
    //http://localhost:8080/api/incentivos/ranking/destacados
    @GetMapping("/ranking/destacados")
    public ResponseEntity<PodioMensualDTO> obtenerPodioDestacado() {
        YearMonth mesActual = YearMonth.now(); 
        return ResponseEntity.ok(this.analiticaService.obtenerPodioDestacadoDelMes(mesActual));
} */
}

