package org.donatrack.dominio.necesidades;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.categoria.Subcategoria;
public abstract class Necesidad {
    
    protected EntidadBeneficiaria entidad;
    protected Subcategoria subcategoria;
    protected String descripcion;
    protected Integer cantidad;
    protected Boolean activa;


    public Integer getCantidad(){
        return cantidad;
    }

    public Subcategoria getSubcategoria(){
        return subcategoria;
    }

    public void marcarComoSaldada(){
        this.activa = false;
    }

}
