package org.donatrack.dominio.donacion;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "estados_entregada")
public class EstadoEntregada extends EstadoDonacion {
    @ManyToOne
    @JoinColumn(name = "entidad_beneficiaria_id")
    private EntidadBeneficiaria entidadBeneficiaria;

    public EstadoEntregada() {
        this.estado = TipoEstado.ENTREGADA;
    }

    public EstadoEntregada(EntidadBeneficiaria entidad) {
        this.estado = TipoEstado.ENTREGADA;
        this.entidadBeneficiaria = entidad;
    }

}
