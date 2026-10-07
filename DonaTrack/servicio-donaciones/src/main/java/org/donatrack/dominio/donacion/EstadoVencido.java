package org.donatrack.dominio.donacion;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "estados_vencido")
public class EstadoVencido extends EstadoDonacion {

    public EstadoVencido() {
        this.estado = TipoEstado.VENCIDA;
    }
}
