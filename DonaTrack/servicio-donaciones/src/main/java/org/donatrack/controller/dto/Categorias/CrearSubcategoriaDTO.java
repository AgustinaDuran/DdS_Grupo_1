package org.donatrack.controller.dto.Categorias;

public class CrearSubcategoriaDTO {

    private String nombre;
    private Long categoriaId;
    private Boolean perecedero;
    private Boolean requiereEstadoUso;

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

    public Boolean getPerecedero() {
        return perecedero;
    }

    public void setPerecedero(Boolean perecedero) {
        this.perecedero = perecedero;
    }

    public Boolean getRequiereEstadoUso() {
        return requiereEstadoUso;
    }

    public void setRequiereEstadoUso(Boolean requiereEstadoUso) {
        this.requiereEstadoUso = requiereEstadoUso;
    }

}
