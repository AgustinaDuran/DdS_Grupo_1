package org.donatrack.dominio.donacion;
import java.time.LocalDateTime;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;


public class EstadoEnDeposito extends EstadoDonacion {

    public EstadoEnDeposito() {
        this.estado = TipoEstado.EN_DEPOSITO;
    }

    @Override
    public void siguiente(Donacion d, EntidadBeneficiaria entidad) {
        EstadoDonacion nuevoEstado = new EstadoAsignacionRealizada();
        nuevoEstado.setDate();
        d.setEstadoDonacion(nuevoEstado);
    }

    @Override
    public void falloEnEstado(Donacion d, String j) {
        EstadoDonacion nuevoEstado = new EstadoVencido();
        nuevoEstado.setDate();
        d.setEstadoDonacion(nuevoEstado);
    }
}