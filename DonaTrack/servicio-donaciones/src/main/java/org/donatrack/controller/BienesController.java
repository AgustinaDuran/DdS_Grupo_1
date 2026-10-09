package org.donatrack.controller;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.controller.dto.Bienes.ActualizarBienDTO;
import org.donatrack.controller.dto.Bienes.CrearBienDTO;
import org.donatrack.controller.dto.Bienes.ResponseBienDTO;
import org.donatrack.dominio.bien.Bien;
import org.donatrack.service.BienesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/bienes")
@CrossOrigin(origins = "*")
public class BienesController {

    private final BienesService bienesService;

    public BienesController(BienesService bienesService) {
        this.bienesService = bienesService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseBienDTO>> obtenerBienes() {
        List<Bien> bienes = bienesService.obtenerBienes();
        List<ResponseBienDTO> bienesDTOs = new ArrayList<>();
        for (Bien bien : bienes) {
            bienesDTOs.add(new ResponseBienDTO(bien));
        }
        return ResponseEntity.ok(bienesDTOs);
    }

    @PostMapping
    public ResponseEntity<String> crearBien(@RequestBody CrearBienDTO crearBienDTO) {
        bienesService.crearBien(crearBienDTO);
        return ResponseEntity.ok("Bien añadido correctamente");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> actualizarBien(@PathVariable Long id, @RequestBody ActualizarBienDTO datosActualizacion) {
        bienesService.actualizarBien(id, datosActualizacion);
        return ResponseEntity.ok("Bien actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarBien(@PathVariable Long id) {
        bienesService.eliminarBienPorId(id);
        return ResponseEntity.ok("Bien eliminado correctamente");
    }

}
