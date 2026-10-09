package org.donatrack.dominio.donacion;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "estados_asignacion_realizada")
public class EstadoAsignacionRealizada extends EstadoDonacion {

    public EstadoAsignacionRealizada() {
        this.estado = TipoEstado.ASIGNACION_REALIZADA;
        this.estadosValidos = List.of(TipoEstado.LISTA_PARA_ENTREGAR);
    }

   @Override
    public void siguiente(Donacion d) {
        EstadoDonacion estadoNuevo = new EstadoListaParaEntregar();
        estadoNuevo.setDate();
        d.setEstadoDonacion(estadoNuevo);
        
    }
}
