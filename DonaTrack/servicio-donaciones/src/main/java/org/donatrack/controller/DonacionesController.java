package org.donatrack.controller;
import org.donatrack.controller.dto.*;
import org.donatrack.dominio.donacion.Donacion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.donatrack.service.DonacionesService;

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
/*
    //http://localhost:8080/api/incentivos/donantes/{nombreUsuario}/misiones-progreso
    @GetMapping("/donantes/{nombreUsuario}/misiones-progreso")
    public ResponseEntity<List<MisionProgresoDTO>> obtenerMisiones(@PathVariable String nombreUsuario) {
        return ResponseEntity.ok(this.analiticaService.obtenerProgresoMisiones(nombreUsuario));
    }

    //http://localhost:8080/api/incentivos/donantes/{nombreUsuario}/vitrina
    @GetMapping("/donantes/{nombreUsuario}/vitrina")
    public ResponseEntity<List<InsigniaDTO>> obtenerVitrina(@PathVariable String nombreUsuario) {
        return ResponseEntity.ok(this.analiticaService.obtenerVitrinaInsignias(nombreUsuario));
    }
    //http://localhost:8080/api/incentivos/ranking/destacados
    @GetMapping("/ranking/destacados")
    public ResponseEntity<PodioMensualDTO> obtenerPodioDestacado() {
        YearMonth mesActual = YearMonth.now(); 
        return ResponseEntity.ok(this.analiticaService.obtenerPodioDestacadoDelMes(mesActual));
} */
}

