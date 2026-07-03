package org.donatrack.service;

import java.util.List;
import java.util.ArrayList;

import org.donatrack.controller.dto.Bienes.ActualizarBienDTO;
import org.donatrack.controller.dto.Bienes.CrearBienDTO;
import org.donatrack.dominio.bien.Bien;
import org.donatrack.dominio.bien.BienEstado;
import org.donatrack.dominio.bien.BienMedible;
import org.donatrack.dominio.bien.BienMediblePerecedero;
import org.donatrack.repository.BienesRepository;
import org.springframework.stereotype.Service;
import org.donatrack.controller.dto.Bienes.ItemBienDTO;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.bien.ItemBien;

import java.time.LocalDate;

@Service
public class BienesService {

    private final BienesRepository bienesRepository;
    private final CategoriasService categoriasService;

    public BienesService(BienesRepository bienesRepository, CategoriasService categoriasService) {
        this.bienesRepository = bienesRepository;
        this.categoriasService = categoriasService;
    }

    public List<Bien> obtenerBienes() {
        return bienesRepository.findAll();
    }

    public Bien crearBien(CrearBienDTO nuevoBien) {
        
        Subcategoria subcategoria = this.categoriasService.obtenerSubcategoriaPorId(nuevoBien.getSubcategoriaId());
        
        Bien bien;
        

        if (nuevoBien.getEstadoUso() != null) {
            bien = new BienEstado(nuevoBien.getNombre(), nuevoBien.getDescripcion(),
                    subcategoria, nuevoBien.getEstadoUso());
        } else if (nuevoBien.getFechaVencimiento() != null) {
            bien = new BienMediblePerecedero(nuevoBien.getNombre(), nuevoBien.getDescripcion(),
                    subcategoria, nuevoBien.getUnidad(), nuevoBien.getCantidadBien(),
                    LocalDate.parse(nuevoBien.getFechaVencimiento()));
        } else {
            bien = new BienMedible(nuevoBien.getNombre(), nuevoBien.getDescripcion(),
                    subcategoria, nuevoBien.getUnidad(), nuevoBien.getCantidadBien());
        }

        bienesRepository.save(bien);
        return bien;
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

    public List<ItemBien> convertirItemsDTOaItemsBien(List<ItemBienDTO> itemsDTO){
        List<ItemBien> ItemBienes = new ArrayList<>();
        System.out.println("ItemsDTO: " + itemsDTO);
        for (ItemBienDTO itemBien : itemsDTO) {
            System.out.println("Subcategoria id: " + itemBien.getSubcategoriaId());

            Subcategoria subcategoria = this.categoriasService.obtenerSubcategoriaPorId(itemBien.getSubcategoriaId());
            if (subcategoria == null) {
                throw new IllegalArgumentException("No se encontró la subcategoría con el ID proporcionado");
            }
            if (itemBien.getBienId() != null) {
                Bien bienExistente = this.bienesRepository.findById(itemBien.getBienId());
                if (bienExistente == null) {
                    throw new IllegalArgumentException("No se encontró el bien con el ID proporcionado");
                }
                ItemBien item = new ItemBien(bienExistente, itemBien.getCantidad());
                ItemBienes.add(item);
            } else {
                CrearBienDTO nuevoBienDTO = new CrearBienDTO();
                nuevoBienDTO.setNombre(itemBien.getNombre());
                nuevoBienDTO.setDescripcion(itemBien.getDescripcion());
                nuevoBienDTO.setFoto(itemBien.getFoto());
                nuevoBienDTO.setSubcategoriaId(itemBien.getSubcategoriaId());
                nuevoBienDTO.setEstadoUso(itemBien.getEstadoUso());
                nuevoBienDTO.setUnidad(itemBien.getUnidad());
                nuevoBienDTO.setCantidadBien(itemBien.getCantidadBien());
                nuevoBienDTO.setFechaVencimiento(itemBien.getFechaVencimiento());

                Bien nuevoBien = this.crearBien(nuevoBienDTO);
                ItemBien item = new ItemBien(nuevoBien, itemBien.getCantidad());
                ItemBienes.add(item);
            }            
        }
        return ItemBienes;
    }

}
