import java.time.LocalDateTime;

public class BienAlimentoPerecedero extends BienAlimento {
    
    private LocalDateTime fechaVencimiento;

    public BienAlimentoPerecedero(String nombre,String descripcion,String foto,TipoUnidad unidad,Float cantidadBien, LocalDateTime vencimiento){
        super(nombre, descripcion, foto,  unidad, cantidadBien);
        this.fechaVencimiento=vencimiento;
    }

    public LocalDateTime getFechaVencimiento(){
        return fechaVencimiento;
    }

    

}
