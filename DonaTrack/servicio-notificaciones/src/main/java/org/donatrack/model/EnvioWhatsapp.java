package org.donatrack.model;

public class EnvioWhatsapp extends MedioEnvio {

    public EnvioWhatsapp(String numeroTelefono) {
        this.destino = numeroTelefono;
    }

    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        throw new MedioNoSoportadoException("WHATSAPP");
    }
}
