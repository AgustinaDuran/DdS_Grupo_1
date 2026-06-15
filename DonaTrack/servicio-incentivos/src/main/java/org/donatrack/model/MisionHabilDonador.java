package org.donatrack.model;

public class MisionHabilDonador extends Mision {
    
    private int cantidadBienesRequerida;

    public MisionHabilDonador(String descripcion, Insignia insignia, int cantidadBienesRequerida) {
        super(descripcion, insignia);
        this.cantidadBienesRequerida = cantidadBienesRequerida;
    }

    @Override
    public Boolean estaCumplidaPor(DonanteIncentivos donante) {
        return donante.calcularImpactoAcumulado() >= this.cantidadBienesRequerida;
    }

    @Override
    public Integer getProgresoActual(DonanteIncentivos donante) {
        return donante.CalcularImpactoAcumulado();
    }

    @Override
        public Integer getObjetivoAsignado() {
        return this.cantidadBienesRequerida;
    }   

}
