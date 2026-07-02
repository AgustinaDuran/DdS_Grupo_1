package org.donatrack.controller;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.controller.dto.EntidadBeneficiaria.CrearEntidadBeneficiariaDTO;
import org.donatrack.controller.dto.EntidadBeneficiaria.EntidadBeneficiariaDTO;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.service.EntidadesBeneficiariasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<EntidadBeneficiariaDTO> obtenerEntidadPorId(@PathVariable Long id) {
        EntidadBeneficiaria entidad = entidadesBeneficiariasService.obtenerEntidadPorId(id);
        if (entidad != null) {
            EntidadBeneficiariaDTO entidadDTO = new EntidadBeneficiariaDTO(entidad);
            return ResponseEntity.ok(entidadDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
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
