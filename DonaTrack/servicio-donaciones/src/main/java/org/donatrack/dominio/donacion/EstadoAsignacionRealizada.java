package org.donatrack.dominio.donacion;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

public class EstadoAsignacionRealizada extends EstadoDonacion {

    public EstadoAsignacionRealizada() {
        this.estado = TipoEstado.ASIGNACION_REALIZADA;
    }

   @Override
    public void siguiente(Donacion d, EntidadBeneficiaria entidad) {
        EstadoDonacion estadoNuevo = new EstadoListaParaEntregar();
        estadoNuevo.setDate();
        d.setEstadoDonacion(estadoNuevo);
        
    }

    
}