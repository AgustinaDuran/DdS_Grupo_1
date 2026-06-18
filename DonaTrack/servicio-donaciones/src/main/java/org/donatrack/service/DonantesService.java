package org.donatrack.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.donatrack.dominio.donante.Donante;
import org.donatrack.repository.DonantesRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class DonantesService {
    
    private DonantesRepository donantesRepository;

    public DonantesService(DonantesRepository donantesRepository) {
        this.donantesRepository = donantesRepository;
    }

    public List<Donante> obtenerDonantes() {
        return donantesRepository.findAll();
    }

    public Optional<Donante> obtenerDonantePorId(Long id) {
        return donantesRepository.findById(id);
    }

    public Donante nuevoDonante(Donante donante) {
        return donantesRepository.save(donante);
    }
    //por ahora son iguales pero nuevoDonante implicará varios chequeos y demas
    public Donante guardarDonante(Donante donante) {
        return donantesRepository.save(donante);
    }

    public void eliminarDonante(Long id) {
        donantesRepository.delete(id);
    }
}
