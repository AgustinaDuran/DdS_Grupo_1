package org.donatrack.dominio.donacion;
import java.time.LocalDateTime;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import java.util.ArrayList;
import java.util.List;

public class EstadoEnDeposito extends EstadoDonacion {

    public EstadoEnDeposito() {
        this.estado = TipoEstado.EN_DEPOSITO;
        this.estadosValidos = List.of(TipoEstado.ASIGNACION_REALIZADA, TipoEstado.VENCIDA);
    }

    @Override
    public void siguiente(Donacion d, EntidadBeneficiaria entidad) {
        EstadoDonacion nuevoEstado = new EstadoAsignacionRealizada();
        nuevoEstado.setDate();
        d.setEstadoDonacion(nuevoEstado);
        d.setEntidadAEntregar(entidad);
    }

    @Override
    public void falloEnEstado(Donacion d) {
        EstadoDonacion nuevoEstado = new EstadoVencido();
        nuevoEstado.setDate();
        d.setEstadoDonacion(nuevoEstado);
    }
}