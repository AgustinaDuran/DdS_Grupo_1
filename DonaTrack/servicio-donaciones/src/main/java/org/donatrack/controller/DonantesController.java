package org.donatrack.controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.service.DonantesService;
import java.time.YearMonth;
import java.util.List;


@RestController
@RequestMapping("/api/donantes")
@CrossOrigin(origins = "*")
public class DonantesController {

    private final DonantesService donantesService;

    public DonantesController(DonantesService donantesService) {
        this.donantesService = donantesService;
    }

    @GetMapping
    public List<Donante> obtenerDonantes() { // aplicar DTO
        return donantesService.obtenerDonantes();
    }

    @PostMapping
    public ResponseEntity<Donante> nuevoDonante(@RequestBody Donante donante) {
        Donante nuevoDonante = donantesService.nuevoDonante(donante);
        return ResponseEntity.ok(nuevoDonante);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarDonante(@PathVariable Long id) {
        donantesService.eliminarDonante(id);
        return ResponseEntity.ok("Donante eliminado exitosamente");
    }

}

