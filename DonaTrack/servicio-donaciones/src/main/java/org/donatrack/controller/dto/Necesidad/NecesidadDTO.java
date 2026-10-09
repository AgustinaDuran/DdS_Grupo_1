package org.donatrack.controller.dto.Necesidad;

import org.donatrack.dominio.necesidades.Necesidad;
import org.donatrack.dominio.necesidades.NecesidadRecurrente;
import org.donatrack.dominio.necesidades.NecesidadExtraordinaria;
import org.donatrack.dominio.necesidades.Periodo;
import org.donatrack.dominio.necesidades.TipoNecesidad;
import org.donatrack.dominio.necesidades.TipoPeriodo;

public class NecesidadDTO {

    private Long id;
    private Long entidadId;
    private Long subcategoriaId;
    private String subcategoriaNombre;
    private String descripcion;
    private Integer cantidad;
    private Boolean activa;
    private TipoNecesidad tipo;

    // Solo para necesidades recurrentes
    private TipoPeriodo tipoPeriodo;
    private Integer frecuencia;

    // Solo para necesidades extraordinarias
    private String motivo;

    public NecesidadDTO(Necesidad necesidad) {
        this.id = necesidad.getId();
        this.descripcion = necesidad.getDescripcion();
        this.cantidad = necesidad.getCantidad();
        this.activa = necesidad.isActiva();

        if (necesidad.getEntidad() != null) {
            this.entidadId = necesidad.getEntidad().getId();
        }
        if (necesidad.getSubcategoria() != null) {
            this.subcategoriaId = necesidad.getSubcategoria().getId();
            this.subcategoriaNombre = necesidad.getSubcategoria().getNombre();
        }

        if (necesidad instanceof NecesidadRecurrente necesidadRecurrente) {
            this.tipo = TipoNecesidad.RECURRENTE;
            Periodo periodo = necesidadRecurrente.getPeriodoTiempo();
            if (periodo != null) {
                this.tipoPeriodo = periodo.getTipoPeriodo();
                this.frecuencia = periodo.getFrecuencia();
            }
        } else if (necesidad instanceof NecesidadExtraordinaria necesidadExtraordinaria) {
            this.tipo = TipoNecesidad.EXTRAORDINARIA;
            this.motivo = necesidadExtraordinaria.getMotivo();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getEntidadId() {
        return entidadId;
    }

    public Long getSubcategoriaId() {
        return subcategoriaId;
    }

    public String getSubcategoriaNombre() {
        return subcategoriaNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Boolean getActiva() {
        return activa;
    }

    public TipoNecesidad getTipo() {
        return tipo;
    }

    public TipoPeriodo getTipoPeriodo() {
        return tipoPeriodo;
    }

    public Integer getFrecuencia() {
        return frecuencia;
    }

    public String getMotivo() {
        return motivo;
    }
}
