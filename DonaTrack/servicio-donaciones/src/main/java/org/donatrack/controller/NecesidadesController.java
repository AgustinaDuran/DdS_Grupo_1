package org.donatrack.controller;
import org.donatrack.controller.dto.*;
import org.donatrack.controller.dto.Necesidad.*;
import org.donatrack.dominio.necesidades.Necesidad;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.donatrack.service.NecesidadesService;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/necesidades")
@CrossOrigin(origins = "*")
public class NecesidadesController {

    private final NecesidadesService necesidadesService;

    public NecesidadesController(NecesidadesService necesidadesService) {
        this.necesidadesService = necesidadesService;
    }


    @GetMapping // ResponseEntity<TipoDeDato>
    public ResponseEntity<List<NecesidadDTO>> obtenerNecesidades(
        FiltrosNecesidadDTO filtrosNecesidadDTO
    ) {
        List<Necesidad> necesidades = necesidadesService.obtenerNecesidades(filtrosNecesidadDTO);
        List<NecesidadDTO> necesidadDTOs = new ArrayList<>();

        for (Necesidad necesidad : necesidades) {
            NecesidadDTO necesidadDTO = new NecesidadDTO(necesidad);
            necesidadDTOs.add(necesidadDTO);
        }
        return ResponseEntity.ok(necesidadDTOs);
    } 
    //http://localhost:8080/api/necesidades -> POST
    @PostMapping
    public ResponseEntity<String> recibirNuevaNecesidad(
        @RequestBody CrearNecesidadDTO nuevaNecesidad) {
        
        necesidadesService.registrarNecesidad(nuevaNecesidad);
        
        return ResponseEntity.ok("Necesidad aniadida correctamente");
    }

    @GetMapping("/{id}")
    public ResponseEntity<NecesidadDTO> obtenerNecesidadPorId(@PathVariable Long id) {
        Necesidad necesidad = necesidadesService.obtenerNecesidadPorId(id);
        if (necesidad != null) {
            NecesidadDTO necesidadDTO = new NecesidadDTO(necesidad);
            return ResponseEntity.ok(necesidadDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> actualizarNecesidad(@PathVariable Long id, @RequestBody ActualizarNecesidadDTO datosActualizacion) {
        necesidadesService.actualizarNecesidad(id, datosActualizacion);
        return ResponseEntity.ok("Necesidad actualizada correctamente");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarNecesidad(@PathVariable Long id) {
        necesidadesService.eliminarNecesidadPorId(id);
        return ResponseEntity.ok("Necesidad eliminada correctamente");
    }

}

