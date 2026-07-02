package org.donatrack.controller.dto.Categorias;

public class ActualizarSubcategoriaDTO {
    private String nombre;
    private String categoriaACambiar;

    public ActualizarSubcategoriaDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoriaACambiar() {
        return categoriaACambiar;
    }

    public void setCategoriaACambiar(String categoriaACambiar) {
        this.categoriaACambiar = categoriaACambiar;
    }
}
