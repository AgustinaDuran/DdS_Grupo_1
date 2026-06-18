package org.donatrack.dominio.donacion;

public class EstadoVencido extends EstadoDonacion {

    public EstadoVencido() {
        this.estado = TipoEstado.VENCIDA;
    }
}