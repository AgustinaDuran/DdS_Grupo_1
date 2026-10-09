package org.donatrack.dominio.necesidades;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.categoria.Subcategoria;

import jakarta.persistence.*;

@Entity
@Table(name = "necesidades")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Necesidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "entidad_id")
    protected EntidadBeneficiaria entidad;

    @ManyToOne
    @JoinColumn(name = "subcategoria_id")
    protected Subcategoria subcategoria;
    protected String descripcion;
    protected Integer cantidad;
    protected Boolean activa;

    protected Necesidad() {
    }


    public Long getId(){
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntidadBeneficiaria getEntidad(){
        return entidad;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public void setCantidad(Integer cantidad){
        this.cantidad = cantidad;
    }

    public void setActiva(Boolean activa){
        this.activa = activa;
    }

    public Integer getCantidad(){
        return cantidad;
    }

    public Subcategoria getSubcategoria(){
        return subcategoria;
    }

    public void marcarComoSaldada(){
        this.activa = false;
    }

    public Boolean isActiva(){
    return activa;
}

}
