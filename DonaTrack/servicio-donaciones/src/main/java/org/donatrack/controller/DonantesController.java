package org.donatrack.controller;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.controller.dto.Donantes.CrearDonanteDTO;
import org.donatrack.controller.dto.Donantes.DonanteDTO;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.service.DonantesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/donantes")
@CrossOrigin(origins = "*")
public class DonantesController {

    private final DonantesService donantesService;

    public DonantesController(DonantesService donantesService) {
        this.donantesService = donantesService;
    }

    @GetMapping
    public ResponseEntity<List<DonanteDTO>> obtenerDonantes() {
        List<Donante> donantes = donantesService.obtenerDonantes();
        List<DonanteDTO> donantesDTOs = new ArrayList<>();
        for (Donante donante : donantes) {
            donantesDTOs.add(new DonanteDTO(donante));
        }
        return ResponseEntity.ok(donantesDTOs);
    }

    @PostMapping
    public ResponseEntity<String> nuevoDonante(@RequestBody CrearDonanteDTO nuevoDonante) {
        donantesService.registrarDonante(nuevoDonante);
        return ResponseEntity.ok("Donante añadido correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarDonante(@PathVariable Long id) {
        donantesService.eliminarDonante(id);
        return ResponseEntity.ok("Donante eliminado exitosamente");
    }

}
