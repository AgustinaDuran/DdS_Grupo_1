package org.donatrack.model;

public class MisionRacha extends Mision {
   
   private Integer mesesConsecutivosRequeridos;
   
   public MisionRacha(String descripcion, Insignia insignia, Integer mesesConsecutivosRequeridos) {
        super(descripcion, insignia);
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
