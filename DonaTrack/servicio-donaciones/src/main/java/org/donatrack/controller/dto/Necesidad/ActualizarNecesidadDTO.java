package org.donatrack.controller.dto.Necesidad;

import org.donatrack.dominio.necesidades.TipoPeriodo;

public class ActualizarNecesidadDTO {

    private String descripcion;
    private Integer cantidad;
    private Boolean activa;

    // Solo para necesidades recurrentes
    private TipoPeriodo tipoPeriodo;
    private Integer frecuencia;

    // Solo para necesidades extraordinarias
    private String motivo;

    public ActualizarNecesidadDTO() {
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

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
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
