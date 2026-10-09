package org.donatrack.dominio.necesidades;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Periodo {

    @Enumerated(EnumType.STRING)
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
