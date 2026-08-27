package org.donatrack.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * El donante todavía no tiene actividad registrada en Incentivos. Es un 404 y no un 500:
 * Incentivos conoce a un donante recién cuando el Servicio de Donaciones le informa su
 * primera donación.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DonanteNoEncontradoException extends RuntimeException {

    public DonanteNoEncontradoException(String nombreUsuario) {
        super("El donante '" + nombreUsuario + "' todavía no registró ninguna donación en Incentivos.");
    }
}
