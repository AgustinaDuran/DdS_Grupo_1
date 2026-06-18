package org.donatrack.model;

public abstract class Mision {
    protected String descripcion;
    protected Insignia insignia;    

    public Mision(String descripcion, Insignia insignia) {
        this.descripcion = descripcion;
        this.insignia = insignia;
    }

    public abstract Boolean estaCumplidaPor(DonanteIncentivos donante);

    public abstract Integer getProgresoActual(DonanteIncentivos donante);

    public abstract Integer getObjetivoAsignado();

    public Integer getDistanciaRestante(DonanteIncentivos donante) {
        Integer restante = this.getObjetivoAsignado() - this.getProgresoActual(donante);
        return Math.max(restante, 0); //no tiene en cuenta negativos
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Insignia getInsigniaOtorgada() {
        return insignia;
    }

}
