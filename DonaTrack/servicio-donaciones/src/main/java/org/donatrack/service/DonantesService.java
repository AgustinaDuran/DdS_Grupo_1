package org.donatrack.service;

import java.util.List;
import java.util.Optional;

import org.donatrack.controller.dto.Donantes.CrearDonanteDTO;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.donante.DonanteJuridico;
import org.donatrack.repository.DonantesRepository;
import org.springframework.stereotype.Service;

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

    public Donante registrarDonante(CrearDonanteDTO nuevoDonante) {
        Donante donante;
        switch (nuevoDonante.getTipo()) {
            case JURIDICA:
                donante = new DonanteJuridico(nuevoDonante.getTipoOrganizacion(), nuevoDonante.getRubro());
                break;
            case HUMANA:
                // El alta de un donante humano requiere un Contacto concreto (correo),
                // pero Contacto es abstracto y aún no tiene una implementación concreta.
                throw new UnsupportedOperationException(
                        "El alta de donante humano aún no está soportada: falta un Contacto concreto en el dominio");
            default:
                throw new IllegalArgumentException("Tipo de donante no válido");
        }
        return donantesRepository.save(donante);
    }

    // por ahora es simple pero guardarDonante implicará varios chequeos y demás
    public Donante guardarDonante(Donante donante) {
        return donantesRepository.save(donante);
    }

    public void eliminarDonante(Long id) {
        donantesRepository.delete(id);
    }
}
