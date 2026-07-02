package org.donatrack.dominio.donacion;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import java.util.ArrayList;
import java.util.List;

public class EstadoEntregaFallida extends EstadoDonacion {

    private String justificacion;
    private EntidadBeneficiaria entidadBeneficiaria;

    public EstadoEntregaFallida() {
        this.estado = TipoEstado.ENTREGA_FALLIDA;
        this.estadosValidos = List.of(TipoEstado.EN_DEPOSITO);
    }

    public EstadoEntregaFallida(EntidadBeneficiaria entidad, String justificacion) {
        this.estado = TipoEstado.ENTREGA_FALLIDA;
        this.estadosValidos = List.of(TipoEstado.EN_DEPOSITO);
        this.justificacion = justificacion;
        this.entidadBeneficiaria = entidad;
    }

    public String getJustificacion() {
        return justificacion;
    }

    @Override
    public void siguiente(Donacion d) {
        EstadoDonacion nuevoEstado = new EstadoEnDeposito();
        nuevoEstado.setDate();
        d.setEstadoDonacion(nuevoEstado);
    }
}