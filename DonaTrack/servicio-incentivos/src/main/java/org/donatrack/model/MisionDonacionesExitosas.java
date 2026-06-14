package org.donatrack.model;

public class MisionDonacionesExitosas extends Mision {
    
    private int cantidadDonacionesExitosasRequerida;

    public MisionDonacionesExitosas(String nombre, String descripcion, Insignia insignia, int cantidadDonacionesRequerida) {
        super(nombre, descripcion, insignia);
        this.cantidadDonacionesExitosasRequerida = cantidadDonacionesRequerida;
    }

    @Override
    public Boolean estaCumplidaPor(DonanteIncentivos donante) {
        return donante.calcularOrganizacionesAyudadas() >= this.cantidadDonacionesExitosasRequerida;
    }
    
    @Override
    public Integer getProgresoActual(DonanteIncentivos donante) {
        return donante.calcularOrganizacionesAyudadas();
    }

    @Override
        public Integer getObjetivoAsignado() {
        return this.cantidadDonacionesExitosasRequerida;
    }

}