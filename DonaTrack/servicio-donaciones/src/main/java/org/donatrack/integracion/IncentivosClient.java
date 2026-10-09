package org.donatrack.integracion;

import org.donatrack.integracion.dto.RegistrarDonacionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Cliente HTTP hacia el Servicio de Incentivos
 * (POST /api/incentivos/registrar-donacion?nombreUsuario=...).
 */
@Component
public class IncentivosClient {

    private static final Logger log = LoggerFactory.getLogger(IncentivosClient.class);

    private final RestClient restClient;

    public IncentivosClient(@Value("${services.incentivos.url:http://localhost:8082}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    /**
     * Informa a Incentivos que un donante realizó una donación, para que impacte en el
     * cálculo de progreso de misiones. Los fallos se registran sin cortar el flujo.
     */
    public void registrarDonacion(String nombreUsuario, RegistrarDonacionRequest request) {
        try {
            restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/incentivos/registrar-donacion")
                            .queryParam("nombreUsuario", nombreUsuario)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("No se pudo registrar la donación de '{}' en Incentivos: {}", nombreUsuario, e.getMessage());
        }
    }
}
