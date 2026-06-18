package org.donatrack.dominio.donacion;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

public class EstadoEnTraslado extends EstadoDonacion {

    public EstadoEnTraslado() {
        this.estado = TipoEstado.EN_TRASLADO;
    }

    @Override
    public boolean entregaActiva() {
    return true;
    }

    @Override
    public void confirmarEntrega(Donacion d) {
        EstadoDonacion nuevoEstado = new EstadoEntregada(d.getEntidadAEntregar());
        nuevoEstado.setDate();
        d.setEstadoDonacion(nuevoEstado);
    }


    @Override
    public void registrarEntregaFallida(Donacion d, String justificacion) {
        d.setEstadoDonacion(new EstadoEntregaFallida(d.getEntidadAEntregar(), justificacion));
    }
}