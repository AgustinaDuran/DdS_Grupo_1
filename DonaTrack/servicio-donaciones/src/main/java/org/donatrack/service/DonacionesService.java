package org.donatrack.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.donatrack.controller.dto.DonacionDTO;
import org.donatrack.repository.DonacionesRepository;

import org.donatrack.dominio.donacion.*;
import org.donatrack.controller.dto.DonacionDTO;
import java.util.ArrayList;
import java.util.List;

@Service
public class DonacionesService {
    private final DonacionesRepository donacionesRepository;
    public DonacionesService(DonacionesRepository donacionesRepository) {
        this.donacionesRepository = donacionesRepository;
    }

    public List<DonacionDTO> obtenerDonaciones() {
        List<Donacion> donaciones = donacionesRepository.findAll();
        
        List<DonacionDTO> donacionDTOs = new ArrayList<>();

        for (Donacion donacion : donaciones) {
            DonacionDTO donacionDTO = new DonacionDTO(donacion);
            donacionDTOs.add(donacionDTO);
        }
        return donacionDTOs;
    }

}