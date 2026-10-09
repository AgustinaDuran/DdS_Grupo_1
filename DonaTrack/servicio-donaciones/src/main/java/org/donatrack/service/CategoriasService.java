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
import org.donatrack.repository.SubcategoriaRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoriasService {

    private final CategoriasRepository categoriasRepository;
    private final SubcategoriaRepository subcategoriaRepository;

    public CategoriasService(CategoriasRepository categoriasRepository, SubcategoriaRepository subcategoriaRepository) {
        this.categoriasRepository = categoriasRepository;
        this.subcategoriaRepository = subcategoriaRepository;
    }

    // ----- Subcategorias -----
    public List<Subcategoria> obtenerSubcategorias(FiltrosSubcategoriaDTO filtros) {
        return subcategoriaRepository.buscarSubcategorias(filtros.getCategoriaId(), filtros.getNombre());
    }

    public Subcategoria obtenerSubcategoriaPorId(Long id) {
        return subcategoriaRepository.findById(id).orElse(null);
    }

    public void registrarSubcategoria(CrearSubcategoriaDTO nuevaSubcategoria) {
        Categoria categoria = null;
        if (nuevaSubcategoria.getCategoriaId() != null) {
            categoria = categoriasRepository.findById(nuevaSubcategoria.getCategoriaId()).orElse(null);
        }
        Subcategoria subcategoria = new Subcategoria(nuevaSubcategoria.getNombre(), categoria);
        subcategoriaRepository.save(subcategoria);
        if (categoria != null) {
            categoria.agregarSubcategoria(subcategoria);
        }
    }

    public void actualizarSubcategoria(Long id, ActualizarSubcategoriaDTO datosActualizacion) {
        Subcategoria subcategoria = subcategoriaRepository.findById(id).orElse(null);
        if (subcategoria == null) {
            throw new IllegalArgumentException("No se encontró la subcategoría con el ID proporcionado");
        }
        if (datosActualizacion.getNombre() != null) {
            subcategoria.setNombre(datosActualizacion.getNombre());
        }
        if (datosActualizacion.getCategoriaId() != null) {
            Categoria categoria = categoriasRepository.findById(datosActualizacion.getCategoriaId()).orElse(null);
            if (categoria == null) {
                throw new IllegalArgumentException("No se encontró la categoría con el ID proporcionado");
            }
            subcategoria.setCategoria(categoria);
        }
        subcategoriaRepository.save(subcategoria);
    }

    public void eliminarSubCategoriaPorId(Long id) {
        subcategoriaRepository.deleteById(id);
    }

    // ----- Categorias -----
    public List<Categoria> obtenerCategorias() {
        return categoriasRepository.findAllCategorias();
    }

    public Categoria obtenerCategoriaId(Long id) {
        return categoriasRepository.findById(id).orElse(null);
    }

    public void registrarCategoria(CrearCategoriaDTO nuevaCategoria) {
        Categoria categoria = new Categoria(nuevaCategoria.getNombre());
        categoriasRepository.saveCategoria(categoria);
    }

    public void actualizarCategoria(Long id, ActualizarCategoriaDTO datosActualizacion) {
        Categoria categoria = categoriasRepository.findById(id).orElse(null);
        if (categoria == null) {
            throw new IllegalArgumentException("No se encontró la categoría con el ID proporcionado");
        }
        if (datosActualizacion.getNombre() != null) {
            categoria.setNombre(datosActualizacion.getNombre());
        }
        categoriasRepository.saveCategoria(categoria);
    }

    public void eliminarCategoriaPorId(Long id) {
        categoriasRepository.deleteCategoria(id);
    }
}
