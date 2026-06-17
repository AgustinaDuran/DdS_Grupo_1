package org.donatrack.dominio.donacion;

public class EstadoListaParaEntregar extends EstadoDonacion {

    @Override
    public void iniciarTraslado(Donacion d) {
        EstadoDonacion estadoNuevo = new EstadoEnTraslado();
        estadoNuevo.setDate();
        d.setEstadoDonacion(estadoNuevo);
    }

}