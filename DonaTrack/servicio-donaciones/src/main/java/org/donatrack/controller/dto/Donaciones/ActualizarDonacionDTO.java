package org.donatrack.controller.dto.Donaciones;

import org.donatrack.dominio.donacion.*;

public class ActualizarDonacionDTO {
    String descripcion; // por ahora no se usa
    String fotoEntrega; 
    TipoEstado tipoEstado;
    String JustificacionEntregaFallida;
    long entidadId;


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
        JustificacionEntregaFallida = JustificacionEntregaFallida;
    }

    public long getEntidadId(){
        return this.entidadId;
    }

    public void setEntidadId(long entidadId){
        this.entidadId = entidadId;
    }



}
