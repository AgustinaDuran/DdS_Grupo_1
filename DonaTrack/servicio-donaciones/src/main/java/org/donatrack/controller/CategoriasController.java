package org.donatrack.controller;

import org.donatrack.controller.dto.Categorias.*;
import org.donatrack.dominio.categoria.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.donatrack.service.DonacionesService;
import org.hibernate.annotations.UpdateTimestamp;

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
    public ResponseEntity<List<SubcategoriaDTO>> getCategorias(
            FiltrosSubcategoriaDTO filtrosSubategoriaDTO) {
        List<Subcategoria> Subcategorias = categoriasService.obtenerSubcategorias(filtrosSubategoriaDTO);
        List<SubcategoriaDTO> SubcategoriasDTOs = new ArrayList<>();

        for (Subcategoria subcategoria : subcategorias) {
            SubcategoriaDTO subcategoriaDTO = new SubcategoriaDTO(subcategoria);
            subcategoriasDTOs.add(subcategoriaDTO);
        }
        return ResponseEntity.ok(SubcategoriasDTOs);
    }

    @PostMapping ("/subcategorias")
    public ResponseEntity<String> crearSubcategoria(@RequestBody CrearSubcategoriaDTO nuevaSubcategoria) {
        categoriasService.registrarSubcategoria(nuevaSubcategoria);
        return ResponseEntity.ok("Subcategoría añadida correctamente");
    }

    @GetMapping("/subcategorias/{id}")
    public ResponseEntity<List<SubcategoriaDTO>> obtenerSubcategoriasPorCategoria(@PathVariable Long id) {
        List<Subcategoria> subcategoria = categoriasService.obtenerSubcategoriaPor(id);
        
        SubcategoriaDTO subcategoriaDTO = new SubcategoriaDTO(subcategoria);

        return ResponseEntity.ok(subcategoriaDTO);
    }

    @DeleteMapping("/subcategorias/{id}")
    public ResponseEntity<String> eliminarSubcategoria(@PathVariable Long id) {
        categoriasService.eliminarSubCategoriaPorId(id);
        return ResponseEntity.ok("Subcategoría eliminada correctamente");
    }

    @PutMapping("/subcategorias/{id}")
    public ResponseEntity<String> actualizarSubcategoria(@PathVariable Long id, @RequestBody ActualizarSubcategoriaDTO datosActualizacion) {
        categoriasService.actualizarSubcategoria(id, datosActualizacion);
        return ResponseEntity.ok("Subcategoría actualizada correctamente");
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
    public ResponseEntity<List<CategoriaDTO>> obtenerCategoriaPorId(@PathVariable Long id) {

        List<Subcategoria> categoria = categoriasService.obtenerCategoriaId(id);

        CategoriaDTO categoriaDTO = new CategoriaDTO(categoria);

        return ResponseEntity.ok(categoriaDTO);
    }

    @PostMapping("/categorias")
    public ResponseEntity<String> crearCategoria(@RequestBody CrearCategoriaDTO nuevaCategoria) {
        categoriasService.registrarCategoria(nuevaCategoria);
        return ResponseEntity.ok("Categoría añadida correctamente");
    }


    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Long id) {
        categoriasService.eliminarCategoriaPorId(id);
        return ResponseEntity.ok("Categoría eliminada correctamente");
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<String> actualizarCategoria(@PathVariable Long id, @RequestBody ActualizarCategoriaDTO datosActualizacion) {
        categoriasService.actualizarCategoria(id, datosActualizacion);
        return ResponseEntity.ok("Categoría actualizada correctamente");
    }



}
