import java.time.LocalDate;

public class PrecioProducto {
    private LocalDate fechaInicioVigencia;
    private LocalDate fechaFinVigencia;
    private float precio;
    
    public LocalDate getFechaInicioVigencia() {
        return fechaInicioVigencia;
    }

    public LocalDate getFechaFinVigencia() {
        return fechaFinVigencia;
    }

    public boolean getCumpleVigencia(LocalDate fecha) {
        return fecha.isAfter(fechaInicioVigencia) && fecha.isBefore(fechaFinVigencia);
    }

    public float getPrecio() {
        return precio;
    }
}