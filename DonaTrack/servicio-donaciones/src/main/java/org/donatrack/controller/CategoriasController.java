package org.donatrack.controller;

import org.donatrack.controller.dto.Categorias.*;
import org.donatrack.dominio.categoria.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.donatrack.service.CategoriasService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")

public class CategoriasController {
    private final CategoriasService categoriasService;

    public CategoriasController(CategoriasService categoriasService) {
        this.categoriasService = categoriasService;
    }

    // SUBCATEGORIAS
    @GetMapping ("/subcategorias") // ResponseEntity<TipoDeDato>
    public ResponseEntity<List<SubcategoriaDTO>> getSubcategorias(
            FiltrosSubcategoriaDTO filtrosSubcategoriaDTO) {
        List<Subcategoria> subcategorias = categoriasService.obtenerSubcategorias(filtrosSubcategoriaDTO);
        List<SubcategoriaDTO> subcategoriasDTOs = new ArrayList<>();

        for (Subcategoria subcategoria : subcategorias) {
            SubcategoriaDTO subcategoriaDTO = new SubcategoriaDTO(subcategoria);
            subcategoriasDTOs.add(subcategoriaDTO);
        }
        return ResponseEntity.ok(subcategoriasDTOs);
    }

    @PostMapping ("/subcategorias")
    public ResponseEntity<SubcategoriaDTO> crearSubcategoria(@RequestBody CrearSubcategoriaDTO nuevaSubcategoria) {
        Subcategoria subcategoria = categoriasService.registrarSubcategoria(nuevaSubcategoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SubcategoriaDTO(subcategoria));
    }

    @GetMapping("/subcategorias/{id}")
    public ResponseEntity<SubcategoriaDTO> obtenerSubcategoriaPorId(@PathVariable Long id) {
        Subcategoria subcategoria = categoriasService.obtenerSubcategoriaPorId(id);
        if (subcategoria == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new SubcategoriaDTO(subcategoria));
    }

    @DeleteMapping("/subcategorias/{id}")
    public ResponseEntity<Void> eliminarSubcategoria(@PathVariable Long id) {
        categoriasService.eliminarSubCategoriaPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/subcategorias/{id}")
    public ResponseEntity<SubcategoriaDTO> actualizarSubcategoria(@PathVariable Long id, @RequestBody ActualizarSubcategoriaDTO datosActualizacion) {
        Subcategoria subcategoria = categoriasService.actualizarSubcategoria(id, datosActualizacion);
        return ResponseEntity.ok(new SubcategoriaDTO(subcategoria));
    }

    // CATEGORIAS
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaDTO>> obtenerCategorias() {
        List<Categoria> categorias = categoriasService.obtenerCategorias();
        List<CategoriaDTO> categoriasDTOs = new ArrayList<>();

        for (Categoria categoria : categorias) {
            CategoriaDTO categoriaDTO = new CategoriaDTO(categoria);
            categoriasDTOs.add(categoriaDTO);
        }
        return ResponseEntity.ok(categoriasDTOs);
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        Categoria categoria = categoriasService.obtenerCategoriaId(id);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CategoriaDTO(categoria));
    }

    @PostMapping("/categorias")
    public ResponseEntity<CategoriaDTO> crearCategoria(@RequestBody CrearCategoriaDTO nuevaCategoria) {
        Categoria categoria = categoriasService.registrarCategoria(nuevaCategoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CategoriaDTO(categoria));
    }


    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriasService.eliminarCategoriaPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(@PathVariable Long id, @RequestBody ActualizarCategoriaDTO datosActualizacion) {
        Categoria categoria = categoriasService.actualizarCategoria(id, datosActualizacion);
        return ResponseEntity.ok(new CategoriaDTO(categoria));
    }



}
