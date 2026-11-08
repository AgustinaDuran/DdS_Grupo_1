package org.donatrack.dominio.algoritmo;

// Se eligió como destino una entidad que no está entre las sugeridas por los algoritmos (HTTP 422).
public class EntidadFueraDelRankingException extends RuntimeException {

    public EntidadFueraDelRankingException(String mensaje) {
        super(mensaje);
    }
}
