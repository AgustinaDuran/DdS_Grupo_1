package org.donatrack.dominio.donacion;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

public class EstadoEntregada extends EstadoDonacion {
    private EntidadBeneficiaria entidadBeneficiaria;

    public EstadoEntregada() {
        this.estado = TipoEstado.ENTREGADA;
    }
    
    public EstadoEntregada(EntidadBeneficiaria e) {
            this.entidadBeneficiaria = e;
        }


}
