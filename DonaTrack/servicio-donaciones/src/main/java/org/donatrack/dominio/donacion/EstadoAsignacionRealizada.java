package org.donatrack.dominio.donacion;

public class EstadoAsignacionRealizada extends EstadoDonacion {

   @Override
    public void planificarRuta(Donacion d) {
        EstadoDonacion estadoNuevo = new EstadoListaParaEntregar();
        estadoNuevo.setDate();
        d.setEstadoDonacion(estadoNuevo);
        
    }

    
}