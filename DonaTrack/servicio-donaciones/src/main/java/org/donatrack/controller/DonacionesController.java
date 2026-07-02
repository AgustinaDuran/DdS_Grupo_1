package org.donatrack.controller;
import org.donatrack.controller.dto.*;
import org.donatrack.controller.dto.Donaciones.ActualizarDonacionDTO;
import org.donatrack.controller.dto.Donaciones.CrearDonacionDTO;
import org.donatrack.controller.dto.Donaciones.DonacionDTO;
import org.donatrack.controller.dto.Donaciones.FiltrosDonacionDTO;
import org.donatrack.dominio.donacion.Donacion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.donatrack.service.DonacionesService;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.YearMonth;
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
        List<DonacionDTO> donacionDTOs = new ArrayList<>();

        for (Donacion donacion : donaciones) {
            DonacionDTO donacionDTO = new DonacionDTO(donacion);
            donacionDTOs.add(donacionDTO);
        }
        return ResponseEntity.ok(donacionDTOs);
    } 
    //http://localhost:8080/api/donaciones -> POST
    @PostMapping
    public ResponseEntity<String> recibirNuevaDonacion(
        @RequestBody CrearDonacionDTO nuevaDonacion) {
        
        donacionesService.registrarDonacion(nuevaDonacion);
        
        return ResponseEntity.ok("Donación aniadida correctamente");
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
    public ResponseEntity<String> actualizarDonacion(@PathVariable Long id, @RequestBody ActualizarDonacionDTO datosActualizacion) {
        donacionesService.actualizarDonacion(id, datosActualizacion);
        return ResponseEntity.ok("Donación actualizada correctamente");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDonacion(@PathVariable Long id) {
        donacionesService.eliminarDonacionPorId(id);
        return ResponseEntity.ok("Donación eliminada correctamente");
    }

}

