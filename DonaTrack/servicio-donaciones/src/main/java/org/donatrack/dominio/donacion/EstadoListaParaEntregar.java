package org.donatrack.dominio.donacion;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import java.util.ArrayList;
import java.util.List;


public class EstadoListaParaEntregar extends EstadoDonacion {

    public EstadoListaParaEntregar() {
        this.estado = TipoEstado.LISTA_PARA_ENTREGAR;
        this.estadosValidos= List.of(TipoEstado.EN_TRASLADO);
    }

    @Override
    public void siguiente(Donacion d) {
        EstadoDonacion estadoNuevo = new EstadoEnTraslado();
        estadoNuevo.setDate();
        d.setEstadoDonacion(estadoNuevo);
    }

}