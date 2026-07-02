package org.donatrack.dominio.necesidades;

public class Periodo {

    private TipoPeriodo tipoPeriodo;
    private Integer frecuencia;

    public Periodo() {
    }

    public Periodo(TipoPeriodo tipoPeriodo, Integer frecuencia) {
        this.tipoPeriodo = tipoPeriodo;
        this.frecuencia = frecuencia;
    }

    public TipoPeriodo getTipoPeriodo() {
        return this.tipoPeriodo;
    }

    public Integer getFrecuencia() {
        return this.frecuencia;
    }
}
