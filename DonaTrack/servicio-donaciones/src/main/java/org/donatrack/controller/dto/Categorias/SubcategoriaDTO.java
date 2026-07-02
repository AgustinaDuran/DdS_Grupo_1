package org.donatrack.controller.dto.Categorias;

import org.donatrack.dominio.categoria.Subcategoria;

public class SubcategoriaDTO {

    private Long id;
    private String nombre;
    private Long categoriaId;
    private String categoriaNombre;

    public SubcategoriaDTO(Subcategoria subcategoria) {
        this.id = subcategoria.getId();
        this.nombre = subcategoria.getNombre();
        if (subcategoria.getCategoria() != null) {
            this.categoriaId = subcategoria.getCategoria().getId();
            this.categoriaNombre = subcategoria.getCategoria().getNombre();
        }
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }
}
