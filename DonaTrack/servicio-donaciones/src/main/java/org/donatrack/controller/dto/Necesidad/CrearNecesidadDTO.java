package org.donatrack.controller.dto.Necesidad;

import org.donatrack.dominio.necesidades.TipoNecesidad;
import org.donatrack.dominio.necesidades.TipoPeriodo;

public class CrearNecesidadDTO {

    private Long entidadId;
    private Long subcategoriaId;
    private String descripcion;
    private Integer cantidad;
    private TipoNecesidad tipo;

    // Solo para necesidades recurrentes
    private TipoPeriodo tipoPeriodo;
    private Integer frecuencia;

    // Solo para necesidades extraordinarias
    private String motivo;

    public CrearNecesidadDTO() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public TipoNecesidad getTipo() {
        return tipo;
    }

    public void setTipo(TipoNecesidad tipo) {
        this.tipo = tipo;
    }

    public TipoPeriodo getTipoPeriodo() {
        return tipoPeriodo;
    }

    public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
    }

    public Integer getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(Integer frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
