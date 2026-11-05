package org.donatrack.controller;
import org.donatrack.controller.dto.Donaciones.ActualizarDonacionDTO;
import org.donatrack.controller.dto.Donaciones.CrearDonacionDTO;
import org.donatrack.controller.dto.Donaciones.DonacionDTO;
import org.donatrack.controller.dto.Donaciones.FiltrosDonacionDTO;
import org.donatrack.dominio.donacion.Donacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.donatrack.service.DonacionesService;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/donaciones")
@CrossOrigin(origins = "*")
public class DonacionesController {

    private final DonacionesService donacionesService;

    public DonacionesController(DonacionesService donacionesService) {
        this.donacionesService = donacionesService;
    }

    //http://localhost:8080/api/donaciones -> GET
    @GetMapping // ResponseEntity<TipoDeDato>
    public ResponseEntity<List<DonacionDTO>> obtenerDonaciones(
        FiltrosDonacionDTO filtrosDonacionesDTO
    ) {
        List<Donacion> donaciones = donacionesService.obtenerDonaciones(filtrosDonacionesDTO);
        return ResponseEntity.ok(aDTOs(donaciones));
    }

    //http://localhost:8080/api/donaciones -> POST
    // La donación se segmenta por subcategoría, por eso se devuelve la lista de donaciones creadas.
    @PostMapping
    public ResponseEntity<List<DonacionDTO>> recibirNuevaDonacion(
        @RequestBody CrearDonacionDTO nuevaDonacion) {

        List<Donacion> creadas = donacionesService.registrarDonacion(nuevaDonacion);

        return ResponseEntity.status(HttpStatus.CREATED).body(aDTOs(creadas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonacionDTO> obtenerDonacionPorId(@PathVariable Long id) {
        Donacion donacion = donacionesService.obtenerDonacionPorId(id);
        if (donacion != null) {
            DonacionDTO donacionDTO = new DonacionDTO(donacion);
            return ResponseEntity.ok(donacionDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DonacionDTO> actualizarDonacion(@PathVariable Long id, @RequestBody ActualizarDonacionDTO datosActualizacion) {
        Donacion donacion = donacionesService.actualizarDonacion(id, datosActualizacion);
        return ResponseEntity.ok(new DonacionDTO(donacion));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDonacion(@PathVariable Long id) {
        donacionesService.eliminarDonacionPorId(id);
        return ResponseEntity.noContent().build();
    }

    private List<DonacionDTO> aDTOs(List<Donacion> donaciones) {
        List<DonacionDTO> donacionDTOs = new ArrayList<>();
        for (Donacion donacion : donaciones) {
            donacionDTOs.add(new DonacionDTO(donacion));
        }
        return donacionDTOs;
    }

}
