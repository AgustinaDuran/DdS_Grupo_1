package org.donatrack.integracion;

import org.donatrack.integracion.dto.NotificacionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Cliente HTTP hacia el Servicio de Notificaciones (POST /api/notificaciones).
 */
@Component
public class NotificacionesClient {

    private static final Logger log = LoggerFactory.getLogger(NotificacionesClient.class);

    private final RestClient restClient;

    public NotificacionesClient(@Value("${services.notificaciones.url:http://localhost:8081}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    /**
     * Envía una notificación. Los fallos del servicio externo se registran pero no
     * interrumpen el flujo de negocio que originó la notificación.
     */
    public void enviar(NotificacionRequest request) {
        try {
            restClient.post()
                    .uri("/api/notificaciones")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("No se pudo enviar la notificación a '{}': {}", request.getNombreDestinatario(), e.getMessage());
        }
    }
}
