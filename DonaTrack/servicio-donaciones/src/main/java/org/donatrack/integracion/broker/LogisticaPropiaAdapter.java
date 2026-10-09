package org.donatrack.integracion.broker;

import org.donatrack.integracion.dto.DepositoRequest;
import org.donatrack.integracion.dto.EntregaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Adapter hacia nuestro Servicio de Logística. Como ya habla el formato de DonaTrack no hace
 * falta traducir: se envían y reciben los DTOs de integracion.dto tal cual.
 * Es el primer proveedor que prueba el broker (@Order(1)).
 */
@Component
@Order(1)
public class LogisticaPropiaAdapter implements ProveedorLogistica {

    private final RestClient restClient;

    public LogisticaPropiaAdapter(@Value("${services.logistica.url:http://localhost:8083}") String baseUrl) {
        // Sin timeout, un proveedor que acepta la conexión y no responde dejaría al broker
        // esperando indefinidamente y nunca pasaría al siguiente.
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    public void registrarEntregas(DepositoRequest request) {
        restClient.post()
                .uri("/api/logistica/entregas")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public List<EntregaResponse> listarEntregas() {
        List<EntregaResponse> entregas = restClient.get()
                .uri("/api/logistica/entregas")
                .retrieve()
                .body(new ParameterizedTypeReference<List<EntregaResponse>>() {});
        return entregas != null ? entregas : Collections.emptyList();
    }

    @Override
    public String nombre() {
        return "logistica propia";
    }
}
