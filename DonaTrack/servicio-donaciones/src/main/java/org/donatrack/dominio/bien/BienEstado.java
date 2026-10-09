package org.donatrack.dominio.bien;

import org.donatrack.dominio.categoria.Subcategoria;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "bienes_estado")
public class BienEstado extends Bien {
    
    private Boolean estadoUso;

    protected BienEstado() {
    }

    public BienEstado(String nombre, String descripcion, Subcategoria subcategoria,Boolean estadoUso){
        super(nombre, descripcion, subcategoria);
        this.estadoUso = estadoUso;
    }

    public Boolean getEstadoUso(){
        return estadoUso;
    }

}
