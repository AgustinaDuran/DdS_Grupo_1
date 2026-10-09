package org.donatrack.controller.dto.Categorias;

import org.donatrack.dominio.categoria.Categoria;


public class CategoriaDTO {
    private String nombre;
    private Long id;

    public CategoriaDTO(Categoria categoria) {
        this.id = categoria.getId();
        this.nombre = categoria.getNombre();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
