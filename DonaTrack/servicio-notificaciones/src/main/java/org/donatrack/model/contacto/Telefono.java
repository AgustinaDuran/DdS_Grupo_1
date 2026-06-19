package org.donatrack.model.contacto;

import org.donatrack.model.EnvioSMS;
import org.donatrack.model.MedioEnvio;

public class Telefono extends Contacto {

    public Telefono(String telefono) {
        this.destino = telefono;
    }

    public String getNroTelefono() {
        return destino;
    }

    @Override
    public MedioEnvio pasarAMedioEnvio() {
        return new EnvioSMS(this.destino);
    }
}
