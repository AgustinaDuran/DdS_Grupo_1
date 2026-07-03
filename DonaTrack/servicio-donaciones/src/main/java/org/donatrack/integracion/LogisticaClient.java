package org.donatrack.integracion;

import org.donatrack.integracion.dto.DepositoRequest;
import org.donatrack.integracion.dto.EntregaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

/**
 * Cliente HTTP hacia el Servicio de Logística.
 * - POST /api/logistica/entregas: entrega la información de las donaciones asignadas
 *   (donaciones "empuja" la info; logística nunca llama a donaciones).
 * - GET /api/logistica/entregas: usado por el polling para detectar cambios de estado.
 */
@Component
public class LogisticaClient {

    private static final Logger log = LoggerFactory.getLogger(LogisticaClient.class);

    private final RestClient restClient;

    public LogisticaClient(@Value("${services.logistica.url:http://localhost:8083}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public void registrarEntregas(DepositoRequest request) {
        try {
            restClient.post()
                    .uri("/api/logistica/entregas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("No se pudieron registrar las entregas en Logística: {}", e.getMessage());
        }
    }

    /**
     * Devuelve todas las entregas conocidas por Logística. En caso de fallo devuelve una
     * lista vacía para que el ciclo de polling no se interrumpa.
     */
    public List<EntregaResponse> listarEntregas() {
        try {
            List<EntregaResponse> entregas = restClient.get()
                    .uri("/api/logistica/entregas")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<EntregaResponse>>() {});
            return entregas != null ? entregas : Collections.emptyList();
        } catch (Exception e) {
            log.warn("No se pudieron consultar las entregas de Logística: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
