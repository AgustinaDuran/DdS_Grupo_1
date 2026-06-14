package org.donatrack.model;

import java.util.function.BooleanSupplier;

public class MisionRacha extends Mision {
   
   private Integer mesesConsecutivosRequeridos;
   
   public MisionRacha(String nombre, String descripcion, Insignia insignia, Integer mesesConsecutivosRequeridos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.insignia = insignia;
        this.mesesConsecutivosRequeridos = mesesConsecutivosRequeridos;
    }

    @Override
    public Boolean estaCumplidaPor(DonanteIncentivos donante) {
        return donante.CalcularRachaActual() >= mesesConsecutivosRequeridos;
    }
    
    @Override
    public Integer getProgresoActual(DonanteIncentivos donante) {
        return donante.CalcularRachaActual();
    }

    @Override
        public Integer getObjetivoAsignado() {
        return this.mesesConsecutivosRequeridos;
    }   
}
