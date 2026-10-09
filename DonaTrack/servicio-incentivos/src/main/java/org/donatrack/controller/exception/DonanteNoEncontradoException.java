package org.donatrack.controller.exception;

public class DonanteNoEncontradoException extends RecursoNoEncontradoException {
    public DonanteNoEncontradoException(String nombreUsuario) {
        super("Donante no encontrado: " + nombreUsuario);
    }
}