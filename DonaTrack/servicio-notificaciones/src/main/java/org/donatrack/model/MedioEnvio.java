package org.donatrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class MedioEnvio {
    protected String destino;
    public abstract void notificar(String nombreDestinatario, String mensaje);

}
