package org.donatrack.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** El parámetro de período no respeta el formato YYYY-MM. */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PeriodoInvalidoException extends RuntimeException {

    public PeriodoInvalidoException(String periodo) {
        super("Período inválido: '" + periodo + "'. Se espera el formato YYYY-MM, por ejemplo 2026-08.");
    }
}
