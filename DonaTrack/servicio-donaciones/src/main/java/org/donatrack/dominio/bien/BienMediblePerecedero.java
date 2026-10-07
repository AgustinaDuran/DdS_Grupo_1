package org.donatrack.dominio.bien;

import org.donatrack.dominio.categoria.Subcategoria;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "bienes_medibles_perecederos")
public class BienMediblePerecedero extends BienMedible {

    private LocalDate fechaVencimiento;

    protected BienMediblePerecedero() {
    }

    public BienMediblePerecedero(String nombre, String descripcion, Subcategoria subcategoria, TipoUnidad unidad, Float cantidadBien,
            LocalDate vencimiento) {
        super(nombre, descripcion, subcategoria, unidad, cantidadBien);
        this.fechaVencimiento = vencimiento;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

}
