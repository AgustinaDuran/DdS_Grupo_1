package org.donatrack.model;

import java.time.LocalDateTime;

public class BienMediblePerecedero extends BienMedible {
    
    private LocalDateTime fechaVencimiento;

    public BienMediblePerecedero(String nombre,String descripcion,String foto,TipoUnidad unidad,Float cantidadBien, LocalDateTime vencimiento){
        super(nombre, descripcion, foto,  unidad, cantidadBien);
        this.fechaVencimiento=vencimiento;
    }

    public LocalDateTime getFechaVencimiento(){
        return fechaVencimiento;
    }

    

}
