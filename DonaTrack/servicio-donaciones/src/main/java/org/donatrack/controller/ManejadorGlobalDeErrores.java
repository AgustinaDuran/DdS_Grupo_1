package org.donatrack.controller;

import org.donatrack.controller.dto.ErrorDTO;
import org.donatrack.dominio.algoritmo.EntidadFueraDelRankingException;
import org.donatrack.service.excepciones.DonacionNoEncontradaException;
import org.donatrack.service.excepciones.RankingNoCalculadoException;
import org.donatrack.service.excepciones.TransicionEstadoInvalidaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// Convierte las excepciones de todo el servicio en respuestas HTTP con un cuerpo ErrorDTO.
//
// Extiende ResponseEntityExceptionHandler para que los errores propios de Spring MVC (JSON mal
// formado, un enum inválido en la URL, un método HTTP no soportado, una ruta inexistente) conserven
// su código HTTP correcto (400, 405, 404...) y salgan con el mismo formato que los nuestros.
//
// Para agregar una excepción nueva: un método @ExceptionHandler que llame a responder(...).
@RestControllerAdvice
public class ManejadorGlobalDeErrores extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ManejadorGlobalDeErrores.class);

    // ---- 404 ----

    @ExceptionHandler(DonacionNoEncontradaException.class)
    public ResponseEntity<Object> donacionNoEncontrada(DonacionNoEncontradaException e) {
        return responder(HttpStatus.NOT_FOUND, "DONACION_NO_ENCONTRADA", e.getMessage());
    }

    // ---- 409 ----

    @ExceptionHandler(TransicionEstadoInvalidaException.class)
    public ResponseEntity<Object> transicionInvalida(TransicionEstadoInvalidaException e) {
        return responder(HttpStatus.CONFLICT, "TRANSICION_INVALIDA", e.getMessage());
    }

    // La lanza Spring cuando el @Version de la fila cambió entre que se leyó y se guardó:
    // otra persona actualizó el mismo registro al mismo tiempo.
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Object> conflictoDeConcurrencia(ObjectOptimisticLockingFailureException e) {
        return responder(HttpStatus.CONFLICT, "CONFLICTO_CONCURRENCIA", "Otra persona ya actualizó esta donación.");
    }

    @ExceptionHandler(RankingNoCalculadoException.class)
    public ResponseEntity<Object> rankingNoCalculado(RankingNoCalculadoException e) {
        return responder(HttpStatus.CONFLICT, "RANKING_NO_CALCULADO", e.getMessage());
    }

    // ---- 422 ----

    @ExceptionHandler(EntidadFueraDelRankingException.class)
    public ResponseEntity<Object> entidadFueraDelRanking(EntidadFueraDelRankingException e) {
        return responder(HttpStatus.UNPROCESSABLE_ENTITY, "ENTIDAD_FUERA_DEL_RANKING", e.getMessage());
    }

    // ---- 400 ----

    // Datos de entrada inválidos o faltantes que detecta el propio código (por ejemplo, falta entidadId).
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> solicitudInvalida(IllegalArgumentException e) {
        return responder(HttpStatus.BAD_REQUEST, "SOLICITUD_INVALIDA", e.getMessage());
    }

    // ---- 500 ----

    // Cualquier otra excepción: se registra completa en el log y al cliente le llega un mensaje
    // genérico, sin detalles internos ni stack trace.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> errorInesperado(Exception e) {
        log.error("Error no controlado", e);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_INTERNO", "Ocurrió un error inesperado. Intentá de nuevo más tarde.");
    }

    // Errores propios de Spring MVC: conserva el código HTTP que elige Spring y usa nuestro formato.
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        HttpStatus status = HttpStatus.resolve(statusCode.value());
        String codigo = status != null ? status.name() : "ERROR";
        String mensaje = statusCode.is4xxClientError()
                ? "La solicitud no es válida."
                : "Ocurrió un error inesperado. Intentá de nuevo más tarde.";
        if (statusCode.is5xxServerError()) {
            log.error("Error de Spring MVC", ex);
        }
        return ResponseEntity.status(statusCode).headers(headers)
                .body(new ErrorDTO(statusCode.value(), codigo, mensaje));
    }

    private ResponseEntity<Object> responder(HttpStatus status, String codigo, String mensaje) {
        return ResponseEntity.status(status).body(new ErrorDTO(status.value(), codigo, mensaje));
    }
}
