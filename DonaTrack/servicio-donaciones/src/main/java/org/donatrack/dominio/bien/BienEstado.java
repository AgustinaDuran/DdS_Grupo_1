package org.donatrack.dominio.bien;

import org.donatrack.dominio.categoria.Subcategoria;

public class BienEstado extends Bien {
    
    private Boolean estadoUso;

    public BienEstado(String nombre, String descripcion, Subcategoria subcategoria,Boolean estadoUso){
        super(nombre, descripcion, subcategoria);
        this.estadoUso = estadoUso;
    }

    public Boolean getEstadoUso(){
        return estadoUso;
    }

}
