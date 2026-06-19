package org.donatrack.model;

public abstract class MedioEnvio {
    protected String destino;

    public abstract void notificar(String nombreDestinatario, String mensaje);

    public String getDestino() {
        return destino;
    }
}
