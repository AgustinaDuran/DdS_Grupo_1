package org.donatrack.controller.dto.Bienes;

import java.time.LocalDateTime;

import org.donatrack.dominio.bien.TipoUnidad;

public class CrearBienDTO {
    private String tipo;
    private String nombre;
    private String descripcion;
    private String foto;
    private Long subcategoriaId;
 
    private Boolean estadoUso;
 
    private TipoUnidad unidad;
    private Float cantidadBien;
 
    private LocalDateTime fechaVencimiento;

 
    public String getTipo() { 
        return tipo; 
    }

    public void setTipo(String tipo) { 
        this.tipo = tipo; 
    }
 
    public String getNombre() { 
        return nombre; 
    }

    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
 
    public String getDescripcion() { 
        return descripcion; 
    }

    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }
 
    public String getFoto() { 
        return foto; 
    }
    public void setFoto(String foto) { 
        this.foto = foto; 
    }
 
    public Long getSubcategoriaId() { 
        return subcategoriaId; 
    }

    public void setSubcategoriaId(Long subcategoriaId) { 
        this.subcategoriaId = subcategoriaId; 
    }
 
    public Boolean getEstadoUso() { 
        return estadoUso; 
    }

    public void setEstadoUso(Boolean estadoUso) { 
        this.estadoUso = estadoUso; 
    }
 
    public TipoUnidad getUnidad() { 
        return unidad; 
    }

    public void setUnidad(TipoUnidad unidad) { 
        this.unidad = unidad; 
    }
 
    public Float getCantidadBien() { 
        return cantidadBien; 
    }

    public void setCantidadBien(Float cantidadBien) { 
        this.cantidadBien = cantidadBien; 
    }
 
    public LocalDateTime getFechaVencimiento() { 
        return fechaVencimiento; 
    }

    public void setFechaVencimiento(LocalDateTime fechaVencimiento) { 
        this.fechaVencimiento = fechaVencimiento; 
    }
}
