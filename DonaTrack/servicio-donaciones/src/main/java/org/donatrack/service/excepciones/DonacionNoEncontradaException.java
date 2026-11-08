package org.donatrack.service.excepciones;

// No existe una donación con el id pedido (HTTP 404).
public class DonacionNoEncontradaException extends RuntimeException {

    public DonacionNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
