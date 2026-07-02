package org.donatrack.model;

public class EnvioWhatsapp extends MedioEnvio {
    public EnvioWhatsapp(String nroTelefono) {
        this.destino = nroTelefono;
    }
    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        //comunicacion con la api
    }
}

