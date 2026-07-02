package org.donatrack.controller.dto.Categorias;

public class CrearSubcategoriaDTO {

    private String nombre;
    private Long categoriaId;

    public CrearSubcategoriaDTO() {
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
