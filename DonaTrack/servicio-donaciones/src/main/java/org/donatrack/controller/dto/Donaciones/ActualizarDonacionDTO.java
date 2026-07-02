package org.donatrack.controller.dto.Donaciones;

import org.donatrack.dominio.donacion.*;

public class ActualizarDonacionDTO {
    String descripcion; // por ahora no se usa
    String fotoEntrega; 
    TipoEstado tipoEstado;
    String JustificacionEntregaFallida;
    Long entidadId;


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

    public String getJustificacionEntregaFallida() {
        return JustificacionEntregaFallida;
    }

    public void setJustificacionEntregaFallida(String JustificacionEntregaFallida) {
        this.JustificacionEntregaFallida = JustificacionEntregaFallida;
    }

    public Long getEntidadId(){
        return this.entidadId;
    }

    public void setEntidadId(Long entidadId){
        this.entidadId = entidadId;
    }



}
