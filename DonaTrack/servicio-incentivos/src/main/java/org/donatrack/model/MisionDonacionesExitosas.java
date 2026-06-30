package org.donatrack.model;

public class MisionDonacionesExitosas extends Mision {
    
    private Integer cantidadDonacionesExitosasRequerida;

    public MisionDonacionesExitosas(String descripcion, Insignia insignia, Integer cantidadDonacionesExitosasRequerida) {
        super(descripcion, insignia);
        this.cantidadDonacionesExitosasRequerida = cantidadDonacionesExitosasRequerida;
    }

    @Override
    public Boolean EstaCumplidaPor(DonanteIncentivos donante) {
        return donante.CalcularOrganizacionesAyudadas() >= this.cantidadDonacionesExitosasRequerida;
    }
    
    @Override
    public Integer GetProgresoActual(DonanteIncentivos donante) {
        return donante.CalcularOrganizacionesAyudadas();
    }

    @Override
        public Integer GetObjetivoAsignado() {
        return this.cantidadDonacionesExitosasRequerida;
    }

}