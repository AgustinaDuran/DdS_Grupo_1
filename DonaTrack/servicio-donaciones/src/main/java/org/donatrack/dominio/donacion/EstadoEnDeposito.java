package org.donatrack.dominio.donacion;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;


public class EstadoEnDeposito extends EstadoDonacion {

    @Override
    public void asignar(Donacion d) {
        EstadoDonacion nuevoEstado = new EstadoAsignacionRealizada();
        nuevoEstado.setDate();
        d.setEstadoDonacion(nuevoEstado);
    }

    @Override
    public void marcarComoVencida(Donacion d) {
        EstadoDonacion nuevoEstado = new EstadoVencido();
        nuevoEstado.setDate();
        d.setEstadoDonacion(nuevoEstado);
    }
}