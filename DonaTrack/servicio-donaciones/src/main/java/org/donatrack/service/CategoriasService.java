package org.donatrack.service;

import java.util.List;

import org.donatrack.controller.dto.Categorias.ActualizarCategoriaDTO;
import org.donatrack.controller.dto.Categorias.ActualizarSubcategoriaDTO;
import org.donatrack.controller.dto.Categorias.CrearCategoriaDTO;
import org.donatrack.controller.dto.Categorias.CrearSubcategoriaDTO;
import org.donatrack.controller.dto.Categorias.FiltrosSubcategoriaDTO;
import org.donatrack.dominio.categoria.Categoria;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.repository.CategoriasRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoriasService {

    private final CategoriasRepository categoriasRepository;

    public CategoriasService(CategoriasRepository categoriasRepository) {
        this.categoriasRepository = categoriasRepository;
    }

    // ----- Subcategorias -----
    public List<Subcategoria> obtenerSubcategorias(FiltrosSubcategoriaDTO filtros) {
        return categoriasRepository.buscarSubcategorias(filtros.getCategoriaId(), filtros.getNombre());
    }

    public Subcategoria obtenerSubcategoriaPorId(Long id) {
        return categoriasRepository.findSubcategoriaById(id);
    }

    public void registrarSubcategoria(CrearSubcategoriaDTO nuevaSubcategoria) {
        Categoria categoria = null;
        if (nuevaSubcategoria.getCategoriaId() != null) {
            categoria = categoriasRepository.findCategoriaById(nuevaSubcategoria.getCategoriaId());
        }
        Subcategoria subcategoria = new Subcategoria(nuevaSubcategoria.getNombre(), categoria);
        categoriasRepository.saveSubcategoria(subcategoria);
        if (categoria != null) {
            categoria.agregarSubcategoria(subcategoria);
        }
    }

    public void actualizarSubcategoria(Long id, ActualizarSubcategoriaDTO datosActualizacion) {
        Subcategoria subcategoria = categoriasRepository.findSubcategoriaById(id);
        if (subcategoria == null) {
            throw new IllegalArgumentException("No se encontró la subcategoría con el ID proporcionado");
        }
        if (datosActualizacion.getNombre() != null) {
            subcategoria.setNombre(datosActualizacion.getNombre());
        }
        // datosActualizacion.getCategoriaACambiar() no se resuelve aún:
        // falta definir si identifica a la categoría por id o por nombre.
    }

    public void eliminarSubCategoriaPorId(Long id) {
        categoriasRepository.deleteSubcategoria(id);
    }

    // ----- Categorias -----
    public List<Categoria> obtenerCategorias() {
        return categoriasRepository.findAllCategorias();
    }

    public Categoria obtenerCategoriaId(Long id) {
        return categoriasRepository.findCategoriaById(id);
    }

    public void registrarCategoria(CrearCategoriaDTO nuevaCategoria) {
        Categoria categoria = new Categoria(nuevaCategoria.getNombre());
        categoriasRepository.saveCategoria(categoria);
    }

    public void actualizarCategoria(Long id, ActualizarCategoriaDTO datosActualizacion) {
        Categoria categoria = categoriasRepository.findCategoriaById(id);
        if (categoria == null) {
            throw new IllegalArgumentException("No se encontró la categoría con el ID proporcionado");
        }
        if (datosActualizacion.getNombre() != null) {
            categoria.setNombre(datosActualizacion.getNombre());
        }
    }

    public void eliminarCategoriaPorId(Long id) {
        categoriasRepository.deleteCategoria(id);
    }
}
