package org.donatrack.dominio.categoria;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {

    private String nombre;
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Subcategoria> subcategorias;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected Categoria() {
        this.subcategorias = new ArrayList<>();
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
        this.subcategorias = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Subcategoria> getSubcategorias() {
        return this.subcategorias;
    }

    public void agregarSubcategoria(Subcategoria subcategoria) {
        this.subcategorias.add(subcategoria);
    }
}
