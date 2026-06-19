package org.donatrack.model.contacto;

import org.donatrack.model.EnvioWhatsapp;
import org.donatrack.model.MedioEnvio;

public class Whatsapp extends Contacto {

    public Whatsapp(String whatsapp) {
        this.destino = whatsapp;
    }

    public String getNroTelefono() {
        return destino;
    }

    @Override
    public MedioEnvio pasarAMedioEnvio() {
        return new EnvioWhatsapp(this.destino);
    }
}
