package org.donatrack.model.contacto;

import org.donatrack.model.MedioEnvio;

public abstract class Contacto {
    protected String destino;

    public abstract MedioEnvio pasarAMedioEnvio();
}
