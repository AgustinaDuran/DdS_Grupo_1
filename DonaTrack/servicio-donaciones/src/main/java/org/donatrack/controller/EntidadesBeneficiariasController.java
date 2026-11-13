package org.donatrack.controller;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.config.IdentidadAutenticada;
import org.donatrack.controller.dto.EntidadBeneficiaria.CrearEntidadBeneficiariaDTO;
import org.donatrack.controller.dto.EntidadBeneficiaria.EntidadBeneficiariaDTO;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.service.EntidadesBeneficiariasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

    @GetMapping("/por-identidad/{keycloakUserId}")
    public ResponseEntity<EntidadBeneficiariaDTO> obtenerPorIdentidad(@PathVariable String keycloakUserId,
                                                                      JwtAuthenticationToken autenticacion) {
        // Una entidad no puede averiguar a qué entidad pertenece otra cuenta.
        if (!IdentidadAutenticada.puedeConsultar(autenticacion, keycloakUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        EntidadBeneficiaria entidad = entidadesBeneficiariasService.obtenerPorIdentidad(keycloakUserId);
        return entidad == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(new EntidadBeneficiariaDTO(entidad));
    }

    @PostMapping
    public ResponseEntity<EntidadBeneficiariaDTO> nuevaEntidad(@RequestBody CrearEntidadBeneficiariaDTO nuevaEntidad) {
        EntidadBeneficiaria entidad = entidadesBeneficiariasService.registrarEntidad(nuevaEntidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EntidadBeneficiariaDTO(entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarEntidad(@PathVariable Long id) {
        entidadesBeneficiariasService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }

}
