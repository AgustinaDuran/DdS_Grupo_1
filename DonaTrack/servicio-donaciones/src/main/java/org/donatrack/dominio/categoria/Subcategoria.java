package org.donatrack.dominio.categoria;
import org.donatrack.dominio.bien.Bien;
import jakarta.persistence.*;

public class Subcategoria{
    private String nombre;
    private Categoria categoria;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Subcategoria(String nombre,Categoria categoria){
        this.nombre= nombre;
        this.categoria= categoria;
    }

    public long getId() {
        return id;
    }

    public Categoria getCategoria(){
        return this.categoria;
    }

    public void setCategoria(Categoria categoria){
        this.categoria = categoria;
    }

    public String getNombre(){
        return this.nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

}