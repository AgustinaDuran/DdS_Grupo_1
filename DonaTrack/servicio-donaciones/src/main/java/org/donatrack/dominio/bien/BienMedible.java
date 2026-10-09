package org.donatrack.dominio.bien;
import org.donatrack.dominio.categoria.Subcategoria;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "bienes_medibles")
public class BienMedible extends Bien {

    @Enumerated(EnumType.STRING)
    protected TipoUnidad unidad;
    protected Float cantidadBien;

    protected BienMedible() {
    }

    public BienMedible(String nombre, String descripcion, Subcategoria subcategoria, TipoUnidad unidad,
            Float cantidadBien) {
        super(nombre, descripcion, subcategoria);
        this.unidad = unidad;
        this.cantidadBien = cantidadBien;
    }

    public TipoUnidad getUnidad() {
        return unidad;
    }

    public Float getCantidadBien() {
        return cantidadBien;
    }

}
