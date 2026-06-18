package org.donatrack.dominio.donacion;

public class EstadoAsignacionRealizada extends EstadoDonacion {

    public EstadoAsignacionRealizada() {
        this.estado = TipoEstado.ASIGNACION_REALIZADA;
    }

   @Override
    public void planificarRuta(Donacion d) {
        EstadoDonacion estadoNuevo = new EstadoListaParaEntregar();
        estadoNuevo.setDate();
        d.setEstadoDonacion(estadoNuevo);
        
    }

    
}