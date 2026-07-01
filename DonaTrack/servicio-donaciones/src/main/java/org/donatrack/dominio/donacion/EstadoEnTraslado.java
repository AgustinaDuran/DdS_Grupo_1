package org.donatrack.dominio.donacion;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import java.util.ArrayList;
import java.util.List;

public class EstadoEnTraslado extends EstadoDonacion {

    public EstadoEnTraslado() {
        this.estado = TipoEstado.EN_TRASLADO;
        this.estadosValidos = List.of(TipoEstado.ENTREGADA, TipoEstado.ENTREGA_FALLIDA);
    }

    @Override
    public void siguiente(Donacion d) {
        EstadoDonacion nuevoEstado = new EstadoEntregada(d.getEntidadAEntregar());
        nuevoEstado.setDate();
        d.setEstadoDonacion(nuevoEstado);
    }


    @Override
    public void falloEnEstado(Donacion d, String justificacion) {
        d.setEstadoDonacion(new EstadoEntregaFallida(d.getEntidadAEntregar(), justificacion));
    }
}