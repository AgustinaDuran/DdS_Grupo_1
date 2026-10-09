package org.donatrack.controller.dto.Categorias;

public class FiltrosSubcategoriaDTO {

    private Long categoriaId;
    private String nombre;

    public FiltrosSubcategoriaDTO() {
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
