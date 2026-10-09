package org.donatrack.controller.exception;

public class RankingNoProcesadoException extends RecursoNoEncontradoException {

    public RankingNoProcesadoException(String periodo) {
        super("Aún no se ha procesado el ranking para: " + periodo);
    }
}