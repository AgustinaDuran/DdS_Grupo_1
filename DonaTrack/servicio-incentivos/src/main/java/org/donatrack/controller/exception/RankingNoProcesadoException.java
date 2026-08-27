package org.donatrack.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RankingNoProcesadoException extends RuntimeException {

    public RankingNoProcesadoException(String periodo) {
        super("Aún no se ha procesado el ranking para: " + periodo);
    }
}