package org.donatrack.controller.dto.Categorias;

public class ActualizarSubcategoriaDTO {
    private String nombre;
    private Long categoriaId;

    public ActualizarSubcategoriaDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
