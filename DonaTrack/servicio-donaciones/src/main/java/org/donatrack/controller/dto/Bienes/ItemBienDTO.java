package org.donatrack.controller.dto.Bienes;

import java.time.LocalDateTime;

import org.donatrack.dominio.bien.TipoUnidad;

public class ItemBienDTO {
    private Long bienId;
    private Integer cantidad;
    private String descripcion;
    private String foto;
    private String nombre;
    private Long subcategoriaId;

    private Boolean estadoUso;

    protected TipoUnidad unidad;
    protected Float cantidadBien;

    private String fechaVencimiento;

    public ItemBienDTO() {
    }

    public Long getBienId() {
        return bienId;
    }
    public void setBienId(Long bienId) {
        this.bienId = bienId;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }
    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
}
