package org.donatrack.dominio.categoria;


import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

public class Categoria {
    
    private String nombre;
    private List<Subcategoria> subcategorias;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Categoria(String nombre){
        this.nombre = nombre;
        this.subcategorias = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getNombre(){
        return this.nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public List<Subcategoria> getSubcategorias(){
        return this.subcategorias;
    }

    public void agregarSubcategoria(Subcategoria subcategoria){
        this.subcategorias.add(subcategoria); 
    }
} 
