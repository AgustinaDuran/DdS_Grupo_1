package org.donatrack.model;

public abstract class MedioEnvio {

    protected String destino;

    public String getDestino() {
        return destino;
    }

    /**
     * Entrega el mensaje al destinatario.
     * Lanza una excepcion si el envio no se concreta.
     */
    public abstract void notificar(String nombreDestinatario, String mensaje);
}
