package org.donatrack.controller;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.controller.dto.EntidadBeneficiaria.CrearEntidadBeneficiariaDTO;
import org.donatrack.controller.dto.EntidadBeneficiaria.EntidadBeneficiariaDTO;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.service.EntidadesBeneficiariasService;
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
@RequestMapping("/api/entidades-beneficiarias")
@CrossOrigin(origins = "*")
public class EntidadesBeneficiariasController {

    private final EntidadesBeneficiariasService entidadesBeneficiariasService;

    public EntidadesBeneficiariasController(EntidadesBeneficiariasService entidadesBeneficiariasService) {
        this.entidadesBeneficiariasService = entidadesBeneficiariasService;
    }

    @GetMapping
    public ResponseEntity<List<EntidadBeneficiariaDTO>> obtenerEntidades() {
        List<EntidadBeneficiaria> entidades = entidadesBeneficiariasService.obtenerEntidades();
        List<EntidadBeneficiariaDTO> entidadesDTOs = new ArrayList<>();
        for (EntidadBeneficiaria entidad : entidades) {
            entidadesDTOs.add(new EntidadBeneficiariaDTO(entidad));
        }
        return ResponseEntity.ok(entidadesDTOs);
    }

    @PostMapping
    public ResponseEntity<String> nuevaEntidad(@RequestBody CrearEntidadBeneficiariaDTO nuevaEntidad) {
        entidadesBeneficiariasService.registrarEntidad(nuevaEntidad);
        return ResponseEntity.ok("Entidad beneficiaria añadida correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarEntidad(@PathVariable Long id) {
        entidadesBeneficiariasService.eliminarEntidad(id);
        return ResponseEntity.ok("Entidad beneficiaria eliminada exitosamente");
    }

}
