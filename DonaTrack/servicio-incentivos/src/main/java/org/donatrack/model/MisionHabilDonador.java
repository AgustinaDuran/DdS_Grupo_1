package org.donatrack.model;

public class MisionHabilDonador extends Mision {
    
    private Integer cantidadBienesRequerida;

    public MisionHabilDonador(String descripcion, Insignia insignia, Integer cantidadBienesRequerida) {
        super(descripcion, insignia);
        this.cantidadBienesRequerida = cantidadBienesRequerida;
    }

    @Override
    public Boolean EstaCumplidaPor(DonanteIncentivos donante) {
        return donante.CalcularImpactoAcumulado() >= this.cantidadBienesRequerida;
    }

    @Override
    public Integer GetProgresoActual(DonanteIncentivos donante) {
        return donante.CalcularImpactoAcumulado();
    }

    @Override
        public Integer GetObjetivoAsignado() {
        return this.cantidadBienesRequerida;
    }   

}
