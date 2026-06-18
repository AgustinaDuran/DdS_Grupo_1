package org.donatrack.controller.dto;
import java.time.LocalDateTime;

import org.donatrack.dominio.categoria.*;
import org.donatrack.dominio.donacion.*;

public class FiltrosDonacionDTO {
    private TipoEstado tipoEstado;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    private Subcategoria subcategoria;

    public FiltrosDonacionDTO() {
    }

    public TipoEstado getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(TipoEstado tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public LocalDateTime getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDateTime fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDateTime getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDateTime fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Subcategoria getSubcategoria() {
        return this.subcategoria;
    }

    public void setSubcategoria(Subcategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

}
