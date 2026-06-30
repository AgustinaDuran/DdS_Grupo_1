package org.donatrack.dominio.donacion;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

public class EstadoEnTraslado extends EstadoDonacion {

    public EstadoEnTraslado() {
        this.estado = TipoEstado.EN_TRASLADO;
    }

    @Override
    public void siguiente(Donacion d, EntidadBeneficiaria entidad) {
        EstadoDonacion nuevoEstado = new EstadoEntregada(d.getEntidadAEntregar());
        nuevoEstado.setDate();
        d.setEstadoDonacion(nuevoEstado);
    }


    @Override
    public void falloEnEstado(Donacion d, String justificacion) {
        d.setEstadoDonacion(new EstadoEntregaFallida(d.getEntidadAEntregar(), justificacion));
    }
}