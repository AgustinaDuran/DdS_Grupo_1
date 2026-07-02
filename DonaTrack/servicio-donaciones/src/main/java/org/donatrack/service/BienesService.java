package org.donatrack.service;

import java.util.List;

import org.donatrack.controller.dto.Bienes.ActualizarBienDTO;
import org.donatrack.controller.dto.Bienes.CrearBienDTO;
import org.donatrack.dominio.bien.Bien;
import org.donatrack.dominio.bien.BienEstado;
import org.donatrack.dominio.bien.BienMedible;
import org.donatrack.dominio.bien.BienMediblePerecedero;
import org.donatrack.repository.BienesRepository;
import org.springframework.stereotype.Service;

@Service
public class BienesService {

    private final BienesRepository bienesRepository;

    public BienesService(BienesRepository bienesRepository) {
        this.bienesRepository = bienesRepository;
    }

    public List<Bien> obtenerBienes() {
        return bienesRepository.findAll();
    }

    public void crearBien(CrearBienDTO nuevoBien) {
        Bien bien;
        if (nuevoBien.getEstadoUso() != null) {
            bien = new BienEstado(nuevoBien.getNombre(), nuevoBien.getDescripcion(),
                    nuevoBien.getFoto(), nuevoBien.getEstadoUso());
        } else if (nuevoBien.getFechaVencimiento() != null) {
            bien = new BienMediblePerecedero(nuevoBien.getNombre(), nuevoBien.getDescripcion(),
                    nuevoBien.getFoto(), nuevoBien.getUnidad(), nuevoBien.getCantidadBien(),
                    nuevoBien.getFechaVencimiento());
        } else {
            bien = new BienMedible(nuevoBien.getNombre(), nuevoBien.getDescripcion(),
                    nuevoBien.getFoto(), nuevoBien.getUnidad(), nuevoBien.getCantidadBien());
        }
        // nuevoBien.getSubcategoriaId() no se asocia aún: Bien no expone setSubcategoria.
        bienesRepository.save(bien);
    }

    public void actualizarBien(Long id, ActualizarBienDTO datosActualizacion) {
        Bien bienExistente = bienesRepository.findById(id);
        if (bienExistente == null) {
            throw new IllegalArgumentException("No se encontró el bien con el ID proporcionado");
        }
        if (datosActualizacion.getNombre() != null) {
            bienExistente.setNombre(datosActualizacion.getNombre());
        }
        if (datosActualizacion.getDescripcion() != null) {
            bienExistente.setDescripcion(datosActualizacion.getDescripcion());
        }
        if (datosActualizacion.getFoto() != null) {
            bienExistente.setFoto(datosActualizacion.getFoto());
        }
        bienesRepository.save(bienExistente);
    }

    public void eliminarBienPorId(Long id) {
        bienesRepository.delete(id);
    }

}
