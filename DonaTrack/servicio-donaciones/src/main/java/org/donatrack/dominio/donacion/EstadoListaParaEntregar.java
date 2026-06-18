package org.donatrack.dominio.donacion;

public class EstadoListaParaEntregar extends EstadoDonacion {

    public EstadoListaParaEntregar() {
        this.estado = TipoEstado.LISTA_PARA_ENTREGAR;
    }

    @Override
    public void iniciarTraslado(Donacion d) {
        EstadoDonacion estadoNuevo = new EstadoEnTraslado();
        estadoNuevo.setDate();
        d.setEstadoDonacion(estadoNuevo);
    }

}