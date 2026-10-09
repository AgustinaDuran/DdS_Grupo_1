package org.donatrack.controller.dto.Necesidad;

import org.donatrack.dominio.necesidades.TipoNecesidad;

public class FiltrosNecesidadDTO {

    private Long entidadId;
    private Long subcategoriaId;
    private TipoNecesidad tipo;
    private Boolean activa;

    public FiltrosNecesidadDTO() {
    }

    public Long getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(Long entidadId) {
        this.entidadId = entidadId;
    }

    public Long getSubcategoriaId() {
        return subcategoriaId;
    }

    public void setSubcategoriaId(Long subcategoriaId) {
        this.subcategoriaId = subcategoriaId;
    }

    public TipoNecesidad getTipo() {
        return tipo;
    }

    public void setTipo(TipoNecesidad tipo) {
        this.tipo = tipo;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
}
