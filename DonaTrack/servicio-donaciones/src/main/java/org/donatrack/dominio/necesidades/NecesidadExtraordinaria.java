package org.donatrack.dominio.necesidades;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.categoria.Subcategoria;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "necesidades_extraordinarias")
public class NecesidadExtraordinaria extends Necesidad {
 
    private String motivo;

    protected NecesidadExtraordinaria() {
    }

    public NecesidadExtraordinaria(EntidadBeneficiaria entidad, Subcategoria subcategoria, String descripcion, Integer cantidad, Boolean activa, String motivo){
        this.entidad = entidad;
        this.subcategoria = subcategoria;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.activa = activa;
        this.motivo = motivo;
    }

    public String getMotivo(){
        return motivo;
    }

    public void setMotivo(String motivo){
        this.motivo = motivo;
    }

}
