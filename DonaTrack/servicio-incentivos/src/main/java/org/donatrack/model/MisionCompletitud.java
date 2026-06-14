package org.donatrack.model;

public class MisionCompletitud extends Mision {
   
   private Integer cantidadDonacionesDistintasRequeridas;

    public MisionCompletitud(String nombre, String descripcion, Insignia insignia, Integer cantidadDonacionesDistintasRequeridas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.insignia = insignia;
        this.cantidadDonacionesDistintasRequeridas = cantidadDonacionesDistintasRequeridas;
    }

    @Override
    public Boolean estaCumplidaPor(DonanteIncentivos donante) {
        return donante.CalcularDonacionesDistintas() >= cantidadDonacionesDistintasRequeridas;
    }

    @Override
    public Integer getProgresoActual(DonanteIncentivos donante) {
        return donante.CalcularDonacionesDistintas();
    }

    @Override
        public Integer getObjetivoAsignado() {
        return this.cantidadDonacionesDistintasRequeridas;
    }
}
