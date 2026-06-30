package org.donatrack.model;

public class MisionRacha extends Mision {
   
   private Integer mesesConsecutivosRequeridos;
   
   public MisionRacha(String descripcion, Insignia insignia, Integer mesesConsecutivosRequeridos) {
        super(descripcion, insignia);
        this.mesesConsecutivosRequeridos = mesesConsecutivosRequeridos;
    }

    @Override
    public Boolean EstaCumplidaPor(DonanteIncentivos donante) {
        return donante.CalcularRachaActual() >= mesesConsecutivosRequeridos;
    }
    
    @Override
    public Integer GetProgresoActual(DonanteIncentivos donante) {
        return donante.CalcularRachaActual();
    }

    @Override
        public Integer GetObjetivoAsignado() {
        return this.mesesConsecutivosRequeridos;
    }   
}
