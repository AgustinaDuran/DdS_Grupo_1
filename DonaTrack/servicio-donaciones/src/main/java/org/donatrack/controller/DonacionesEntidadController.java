package org.donatrack.controller;

import java.util.List;

import org.donatrack.controller.dto.PaginaDTO;
import org.donatrack.controller.dto.Donaciones.DonacionDTO;
import org.donatrack.dominio.donacion.TipoEstado;
import org.donatrack.service.AsignacionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Donaciones asignadas a una entidad beneficiaria. Está separado de EntidadesBeneficiariasController
// para que el flujo de asignación no edite el controller de otro flujo.
// TODO flujo 1: una entidad solo puede ver sus propias donaciones (403 si el id no es el del token).
@RestController
@CrossOrigin(origins = "*")
public class DonacionesEntidadController {

    private final AsignacionesService asignacionesService;

    public DonacionesEntidadController(AsignacionesService asignacionesService) {
        this.asignacionesService = asignacionesService;
    }

    // http://localhost:8080/api/entidades-beneficiarias/4/donaciones?estado=ASIGNACION_REALIZADA&estado=EN_TRASLADO&page=0&size=20 -> GET
    // 404 si la entidad no existe.
    @GetMapping("/api/entidades-beneficiarias/{id}/donaciones")
    public ResponseEntity<PaginaDTO<DonacionDTO>> obtenerDonaciones(
            @PathVariable Long id,
            @RequestParam(name = "estado", required = false) List<TipoEstado> estados,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return asignacionesService.obtenerDonacionesDeEntidad(id, estados, page, size)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
