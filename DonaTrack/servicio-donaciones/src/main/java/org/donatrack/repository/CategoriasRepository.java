package org.donatrack.repository;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.dominio.categoria.Categoria;
import org.donatrack.dominio.categoria.Subcategoria;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriasRepository {

    private List<Categoria> categorias;
    private List<Subcategoria> subcategorias;
    private Long nextId = 1L;

    public CategoriasRepository() {
        categorias = new ArrayList<>();
        subcategorias = new ArrayList<>();
    }

    // ----- Categorias -----
    public List<Categoria> findAllCategorias() {
        return categorias;
    }

    public Categoria findCategoriaById(Long id) {
        return categorias.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void saveCategoria(Categoria categoria) {
        if (categoria.getId() == null) {
            categoria.setId(nextId++);
        }
        categorias.add(categoria);
    }

    public void deleteCategoria(Long id) {
        categorias = categorias.stream()
                .filter(c -> c.getId() != id)
                .toList();
    }

    // ----- Subcategorias -----
    public List<Subcategoria> findAllSubcategorias() {
        return subcategorias;
    }

    public Subcategoria findSubcategoriaById(Long id) {
        return subcategorias.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Subcategoria> buscarSubcategorias(Long categoriaId, String nombre) {
        return subcategorias;
    }

    public void saveSubcategoria(Subcategoria subcategoria) {
        if (subcategoria.getId() == null) {
            subcategoria.setId(nextId++);
        }
        subcategorias.add(subcategoria);
    }

    public void deleteSubcategoria(Long id) {
        subcategorias = subcategorias.stream()
                .filter(s -> s.getId() != id)
                .toList();
    }

}

/*
// Variante como bean de Spring en memoria (para que funcione la inyección por constructor)
package org.donatrack.repository;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.dominio.categoria.Categoria;
import org.donatrack.dominio.categoria.Subcategoria;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriasRepository {

    private List<Categoria> categorias;
    private List<Subcategoria> subcategorias;

    public CategoriasRepository() {
        categorias = new ArrayList<>();
        subcategorias = new ArrayList<>();
    }

    public List<Categoria> findAllCategorias() { return categorias; }
    public Categoria findCategoriaById(Long id) {
        return categorias.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
    public void saveCategoria(Categoria categoria) { categorias.add(categoria); }
    public void deleteCategoria(Long id) {
        categorias = categorias.stream().filter(c -> c.getId() != id).toList();
    }

    public List<Subcategoria> findAllSubcategorias() { return subcategorias; }
    public Subcategoria findSubcategoriaById(Long id) {
        return subcategorias.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }
    public List<Subcategoria> buscarSubcategorias(Long categoriaId, String nombre) { return subcategorias; }
    public void saveSubcategoria(Subcategoria subcategoria) { subcategorias.add(subcategoria); }
    public void deleteSubcategoria(Long id) {
        subcategorias = subcategorias.stream().filter(s -> s.getId() != id).toList();
    }
}
*/
