package org.donatrack.service.excepciones;

// Se intentó asignar una donación que todavía no tiene ranking guardado (HTTP 409).
public class RankingNoCalculadoException extends RuntimeException {

    public RankingNoCalculadoException(String mensaje) {
        super(mensaje);
    }
}
