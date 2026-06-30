package org.donatrack.dominio.donacion;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

public class EstadoListaParaEntregar extends EstadoDonacion {

    public EstadoListaParaEntregar() {
        this.estado = TipoEstado.LISTA_PARA_ENTREGAR;
    }

    @Override
    public void siguiente(Donacion d, EntidadBeneficiaria entidad) {
        EstadoDonacion estadoNuevo = new EstadoEnTraslado();
        estadoNuevo.setDate();
        d.setEstadoDonacion(estadoNuevo);
    }

}