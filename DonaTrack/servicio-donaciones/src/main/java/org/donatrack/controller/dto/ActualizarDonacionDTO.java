package org.donatrack.controller.dto;

import org.donatrack.dominio.donacion.*;

public class ActualizarDonacionDTO {
    String descripcion; // por ahora no se usa
    String fotoEntrega; 
    TipoEstado tipoEstado;
    String JustificacionEntregaFallida;


    public ActualizarDonacionDTO() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoEntrega() {
        return fotoEntrega;
    }

    public void setFotoEntrega(String fotoEntrega) {
        this.fotoEntrega = fotoEntrega;
    }

    public TipoEstado getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(TipoEstado tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public String getJustificacionEntregaFallida() {
        return JustificacionEntregaFallida;
    }

    public void setJustificacionEntregaFallida(String JustificacionEntregaFallida) {
        JustificacionEntregaFallida = JustificacionEntregaFallida;
    }

}
