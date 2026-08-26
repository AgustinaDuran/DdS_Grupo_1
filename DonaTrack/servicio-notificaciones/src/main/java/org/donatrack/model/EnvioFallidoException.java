package org.donatrack.model;

/** El medio de envio esta implementado pero el envio no se pudo concretar. */
public class EnvioFallidoException extends RuntimeException {

    public EnvioFallidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
