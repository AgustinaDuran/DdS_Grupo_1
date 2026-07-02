package org.donatrack.controller.dto.Bienes;

import java.time.LocalDateTime;

import org.donatrack.dominio.bien.Bien;
import org.donatrack.dominio.bien.BienEstado;
import org.donatrack.dominio.bien.BienMedible;
import org.donatrack.dominio.bien.BienMediblePerecedero;
import org.donatrack.dominio.bien.TipoUnidad;

public class ResponseBienDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String foto;
    private Long subcategoriaId;

    // Solo BienEstado
    private Boolean estadoUso;

    // Solo BienMedible / BienMediblePerecedero
    private TipoUnidad unidad;
    private Float cantidadBien;

    // Solo BienMediblePerecedero
    private LocalDateTime fechaVencimiento;

    public ResponseBienDTO(Bien bien) {
        this.id = bien.getId();
        this.nombre = bien.getNombre();
        this.descripcion = bien.getDescripcion();
        this.foto = bien.getFoto();
        if (bien.getSubcategoria() != null) {
            this.subcategoriaId = bien.getSubcategoria().getId();
        }

        if (bien instanceof BienEstado bienEstado) {
            this.estadoUso = bienEstado.getEstadoUso();
        }
        if (bien instanceof BienMedible bienMedible) {
            this.unidad = bienMedible.getUnidad();
            this.cantidadBien = bienMedible.getCantidadBien();
        }
        if (bien instanceof BienMediblePerecedero perecedero) {
            this.fechaVencimiento = perecedero.getFechaVencimiento();
        }
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public Long getSubcategoriaId() {
        return subcategoriaId;
    }

    public Boolean getEstadoUso() {
        return estadoUso;
    }

    public TipoUnidad getUnidad() {
        return unidad;
    }

    public Float getCantidadBien() {
        return cantidadBien;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }
}
