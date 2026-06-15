package org.donatrack.model;

public class MisionDonacionesExitosas extends Mision {
    
    private int cantidadDonacionesExitosasRequerida;

    public MisionDonacionesExitosas(String descripcion, Insignia insignia, int cantidadDonacionesExitosasRequerida) {
        super(descripcion, insignia);
        this.cantidadDonacionesExitosasRequerida = cantidadDonacionesExitosasRequerida;
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