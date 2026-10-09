package org.donatrack.controller;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.controller.dto.Donantes.CrearDonanteDTO;
import org.donatrack.controller.dto.Donantes.DonanteDTO;
import org.donatrack.controller.dto.Donantes.ImportacionCsvResultadoDTO;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.service.DonantesService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
        System.out.println("DTO RECIBIDO: " + nuevoDonante);
        System.out.println("ROL USUARIO: " + nuevoDonante.getTipoUsuario());
        

        donantesService.registrarDonante(nuevoDonante);
        return ResponseEntity.ok("Donante añadido correctamente");
    }

    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportacionCsvResultadoDTO> importarDonantes(@RequestParam("file") MultipartFile file) {
        ImportacionCsvResultadoDTO resultado = donantesService.importarDonantesMasivo(file);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonanteDTO> obtenerDonantePorId(@PathVariable Long id) {
        Donante donante = donantesService.obtenerDonantePorId(id).orElse(null);
        if (donante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new DonanteDTO(donante));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarDonante(@PathVariable Long id) {
        donantesService.eliminarDonante(id);
        return ResponseEntity.ok("Donante eliminado exitosamente");
    }

}
