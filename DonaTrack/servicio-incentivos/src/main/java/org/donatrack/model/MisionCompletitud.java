package org.donatrack.model;

public class MisionCompletitud extends Mision {
   
   private Integer cantidadDonacionesDistintasRequeridas;

    public MisionCompletitud(String descripcion, Insignia insignia, Integer cantidadDonacionesDistintasRequeridas) {
        super(descripcion, insignia);
        this.cantidadDonacionesDistintasRequeridas = cantidadDonacionesDistintasRequeridas;
    }

    @Override
    public Boolean EstaCumplidaPor(DonanteIncentivos donante) {
        return donante.CalcularDonacionesDistintas() >= cantidadDonacionesDistintasRequeridas;
    }

    @Override
    public Integer GetProgresoActual(DonanteIncentivos donante) {
        return donante.CalcularDonacionesDistintas();
    }

    @Override
        public Integer GetObjetivoAsignado() {
        return this.cantidadDonacionesDistintasRequeridas;
    }
}
