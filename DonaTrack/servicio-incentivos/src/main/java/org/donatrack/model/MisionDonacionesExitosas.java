package org.donatrack.model;

public class MisionDonacionesExitosas extends Mision {
    
    private Integer cantidadDonacionesExitosasRequerida;

    public MisionDonacionesExitosas(String descripcion, Insignia insignia, Integer cantidadDonacionesExitosasRequerida) {
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