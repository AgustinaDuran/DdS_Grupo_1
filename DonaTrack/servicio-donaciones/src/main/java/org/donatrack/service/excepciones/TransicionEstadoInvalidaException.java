package org.donatrack.service.excepciones;

// La donación no puede pasar al estado pedido desde el estado en que está; por ejemplo,
// porque otra persona ya la asignó (HTTP 409).
public class TransicionEstadoInvalidaException extends RuntimeException {

    public TransicionEstadoInvalidaException(String mensaje) {
        super(mensaje);
    }
}
