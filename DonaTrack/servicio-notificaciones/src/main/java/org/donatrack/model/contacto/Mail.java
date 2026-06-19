package org.donatrack.model.contacto;

import org.donatrack.model.EnvioMail;
import org.donatrack.model.MedioEnvio;

public class Mail extends Contacto {

    public Mail(String email) {
        this.destino = email;
    }

    public String getDireccionMail() {
        return destino;
    }

    @Override
    public MedioEnvio pasarAMedioEnvio() {
        return new EnvioMail(this.destino);
    }
}
