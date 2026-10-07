package org.donatrack.dominio.categoria;
import jakarta.persistence.*;

@Entity
@Table(name = "subcategorias")
public class Subcategoria{
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected Subcategoria() {
    }

    public Subcategoria(String nombre,Categoria categoria){
        this.nombre= nombre;
        this.categoria= categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
