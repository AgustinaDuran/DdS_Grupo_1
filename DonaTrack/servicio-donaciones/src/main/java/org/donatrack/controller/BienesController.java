package org.donatrack.controller;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.controller.dto.Bienes.ActualizarBienDTO;
import org.donatrack.controller.dto.Bienes.CrearBienDTO;
import org.donatrack.controller.dto.Bienes.ResponseBienDTO;
import org.donatrack.dominio.bien.Bien;
import org.donatrack.service.BienesService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBienDTO> obtenerBienPorId(@PathVariable Long id) {
        Bien bien = bienesService.obtenerBienPorId(id);
        if (bien == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseBienDTO(bien));
    }

    @PostMapping
    public ResponseEntity<ResponseBienDTO> crearBien(@RequestBody CrearBienDTO crearBienDTO) {
        Bien bien = bienesService.crearBien(crearBienDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseBienDTO(bien));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseBienDTO> actualizarBien(@PathVariable Long id, @RequestBody ActualizarBienDTO datosActualizacion) {
        Bien bien = bienesService.actualizarBien(id, datosActualizacion);
        return ResponseEntity.ok(new ResponseBienDTO(bien));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBien(@PathVariable Long id) {
        bienesService.eliminarBienPorId(id);
        return ResponseEntity.noContent().build();
    }

}
