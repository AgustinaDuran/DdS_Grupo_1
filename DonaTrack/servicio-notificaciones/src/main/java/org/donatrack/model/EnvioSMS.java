package org.donatrack.model;

public class EnvioSMS extends MedioEnvio {

    public EnvioSMS(String numeroTelefono) {
        this.destino = numeroTelefono;
    }

    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        throw new MedioNoSoportadoException("SMS");
    }
}
