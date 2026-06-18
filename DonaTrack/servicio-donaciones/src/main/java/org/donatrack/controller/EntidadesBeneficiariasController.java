package org.donatrack.controller;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.service.DonacionesService;
import org.donatrack.service.EntidadesBeneficiariasService;
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
import org.donatrack.service.NecesidadesService;
import java.time.YearMonth;
import java.util.List;


@RestController
@RequestMapping("/api/entidades-beneficiarias")
@CrossOrigin(origins = "*")
public class EntidadesBeneficiariasController {

    private final EntidadesBeneficiariasService entidadesBeneficiariasService;

    public EntidadesBeneficiariasController(EntidadesBeneficiariasService entidadesBeneficiariasService) {
        this.entidadesBeneficiariasService = entidadesBeneficiariasService;
    }

    @GetMapping
    public List<EntidadBeneficiaria> obtenerEntidades() {
        return entidadesBeneficiariasService.obtenerEntidades();
    }

    @PostMapping
    public ResponseEntity<EntidadBeneficiaria> nuevoDonante(@RequestBody EntidadBeneficiaria donante) {
        EntidadBeneficiaria nuevaEntidad = entidadesBeneficiariasService.nuevaEntidad(donante);
        return ResponseEntity.ok(nuevaEntidad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarDonante(@PathVariable Long id) {
        entidadesBeneficiariasService.eliminarEntidad(id);
        return ResponseEntity.ok("Donante eliminado exitosamente");
    }



}

