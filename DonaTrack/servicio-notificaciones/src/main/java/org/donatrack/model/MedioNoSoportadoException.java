package org.donatrack.model;

/** El medio de envio existe en el modelo pero todavia no esta implementado. */
public class MedioNoSoportadoException extends RuntimeException {

    public MedioNoSoportadoException(String medio) {
        super("El medio de envio '" + medio + "' todavia no esta implementado.");
    }
}
