package org.donatrack.dominio.bien;
import org.donatrack.dominio.categoria.Subcategoria;
import jakarta.persistence.*;


public abstract class Bien {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String nombre;
    protected String descripcion;
    protected String foto;
    protected Subcategoria subcategoria;

    public String getNombre(){
        return this.nombre;
    }

    public String getDescripcion(){
        return this.descripcion;
    }
    
    public String getFoto(){
        return this.foto;
    }

    public Subcategoria getSubcategoria() {
        return subcategoria;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    

}
