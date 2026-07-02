package org.donatrack.model;

public class EnvioSMS extends MedioEnvio {

    public EnvioSMS(String nroTelefono) {
        this.destino = nroTelefono;
    }
    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        //comunicacion con la api


    }
}
