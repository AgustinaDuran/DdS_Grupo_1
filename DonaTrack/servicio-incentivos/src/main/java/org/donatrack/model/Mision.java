package org.donatrack.model;

public abstract class Mision {
    protected String descripcion;
    protected Insignia insignia;    

    public Mision(String descripcion, Insignia insignia) {
        this.descripcion = descripcion;
        this.insignia = insignia;
    }

    public abstract Boolean EstaCumplidaPor(DonanteIncentivos donante);

    public abstract Integer GetProgresoActual(DonanteIncentivos donante);

    public abstract Integer GetObjetivoAsignado();

    public Integer GetDistanciaRestante(DonanteIncentivos donante) {
        Integer restante = this.GetObjetivoAsignado() - this.GetProgresoActual(donante);
        return Math.max(restante, 0); //no tiene en cuenta negativos
    }

    public String GetDescripcion() { return descripcion; }

    public Insignia GetInsigniaOtorgada() { return insignia; }

}
