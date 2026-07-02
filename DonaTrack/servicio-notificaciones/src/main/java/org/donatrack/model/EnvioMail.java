package org.donatrack.model;

public class EnvioMail extends MedioEnvio {
    

    public EnvioMail(String direccionMail) {
        this.destino = direccionMail;
    }
    
    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        //comunicacion con la api
    }

}
