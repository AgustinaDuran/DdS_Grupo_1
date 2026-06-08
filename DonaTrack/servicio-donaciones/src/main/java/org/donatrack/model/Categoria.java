package org.donatrack.model;

import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private String nombre;
    private List<Subcategoria> subcategorias;

    public Categoria(String nombre){
        this.nombre = nombre;
        this.subcategorias = new ArrayList<>();
    }

    public String getNombre(){
        return this.nombre;
    }

    public void agregarSubcategoria(Subcategoria subcategoria){
        this.subcategorias.add(subcategoria);
        return 
    }
} 
