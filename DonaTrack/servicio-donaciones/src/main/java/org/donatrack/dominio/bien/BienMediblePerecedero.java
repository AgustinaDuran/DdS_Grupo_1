package org.donatrack.dominio.bien;

import org.donatrack.dominio.categoria.Subcategoria;
import java.time.LocalDate;

public class BienMediblePerecedero extends BienMedible {

    private LocalDate fechaVencimiento;

    public BienMediblePerecedero(String nombre, String descripcion, Subcategoria subcategoria, TipoUnidad unidad, Float cantidadBien,
            LocalDate vencimiento) {
        super(nombre, descripcion, subcategoria, unidad, cantidadBien);
        this.fechaVencimiento = vencimiento;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

}
