package org.donatrack.dominio.categoria;
import jakarta.persistence.*;

@Entity
@Table(name = "subcategorias")
public class Subcategoria{
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    private Boolean perecedero;
    private Boolean requiereEstadoUso;

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

    public boolean isPerecedero() {
        return Boolean.TRUE.equals(perecedero);
    }

    public void setPerecedero(Boolean perecedero) {
        this.perecedero = perecedero;
    }

    public boolean isRequiereEstadoUso() {
        return Boolean.TRUE.equals(requiereEstadoUso);
    }

    public void setRequiereEstadoUso(Boolean requiereEstadoUso) {
        this.requiereEstadoUso = requiereEstadoUso;
    }

}
