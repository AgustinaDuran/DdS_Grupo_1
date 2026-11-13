package org.donatrack.controller;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.config.IdentidadAutenticada;
import org.donatrack.controller.dto.Donantes.ActualizarDonanteDTO;
import org.donatrack.controller.dto.Donantes.CrearDonanteDTO;
import org.donatrack.controller.dto.Donantes.DonanteDTO;
import org.donatrack.controller.dto.Donantes.ImportacionCsvResultadoDTO;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.service.DonantesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    public ResponseEntity<List<DonanteDTO>> obtenerDonantes(@RequestParam(value = "q", required = false) String q) {
        List<Donante> donantes = donantesService.buscarDonantes(q);
        List<DonanteDTO> donantesDTOs = new ArrayList<>();
        for (Donante donante : donantes) {
            donantesDTOs.add(new DonanteDTO(donante));
        }
        return ResponseEntity.ok(donantesDTOs);
    }

    @PostMapping
    public ResponseEntity<DonanteDTO> nuevoDonante(@RequestBody CrearDonanteDTO nuevoDonante) {
        Donante donante = donantesService.registrarDonante(nuevoDonante);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DonanteDTO(donante));
    }

    @PostMapping(value = "/importaciones", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportacionCsvResultadoDTO> importarDonantes(@RequestParam("file") MultipartFile file) {
        ImportacionCsvResultadoDTO resultado = donantesService.importarDonantesMasivo(file);
        return ResponseEntity.ok(resultado);
    }

    /** Donante de la cuenta que inició sesión (claim "sub" de Keycloak). */
    @GetMapping("/por-identidad/{keycloakUserId}")
    public ResponseEntity<DonanteDTO> obtenerPorIdentidad(@PathVariable String keycloakUserId,
                                                          JwtAuthenticationToken autenticacion) {
        if (!IdentidadAutenticada.puedeConsultar(autenticacion, keycloakUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // La vinculación por email solo se hace para la propia cuenta, nunca cuando consulta ADMIN.
        String email = IdentidadAutenticada.esPropia(autenticacion, keycloakUserId)
                ? IdentidadAutenticada.emailVerificado(autenticacion)
                : null;
        return donantesService.obtenerPorIdentidad(keycloakUserId, email)
                .map(donante -> ResponseEntity.ok(new DonanteDTO(donante)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonanteDTO> obtenerDonantePorId(@PathVariable Long id) {
        Donante donante = donantesService.obtenerDonantePorId(id).orElse(null);
        if (donante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new DonanteDTO(donante));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DonanteDTO> actualizarDonante(
            @PathVariable Long id, @RequestBody ActualizarDonanteDTO datosActualizacion) {
        Donante donante = donantesService.actualizarDonante(id, datosActualizacion);
        if (donante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new DonanteDTO(donante));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarDonante(@PathVariable Long id) {
        donantesService.eliminarDonante(id);
        return ResponseEntity.noContent().build();
    }

}
