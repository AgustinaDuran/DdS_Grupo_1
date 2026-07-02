package org.donatrack.dominio.bien;
import org.donatrack.dominio.categoria.Subcategoria;

public class BienMedible extends Bien {

    protected TipoUnidad unidad;
    protected Float cantidadBien;

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
