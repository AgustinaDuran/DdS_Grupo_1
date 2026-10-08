package org.donatrack.integracion.broker;

import org.donatrack.integracion.broker.mock.ShipmentRequest;
import org.donatrack.integracion.broker.mock.ShipmentResponse;
import org.donatrack.integracion.dto.DepositoRequest;
import org.donatrack.integracion.dto.EntregaRequest;
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
 * Adapter hacia el proveedor de logística externo (servicio-logistica-mock). Su API es distinta
 * a la de DonaTrack: recibe un envío por request en /v1/shipments, con otros nombres de campos y
 * estados en inglés. Este adapter traduce en ambos sentidos.
 * Es el proveedor de respaldo del broker (@Order(2)).
 */
@Component
@Order(2)
public class LogisticaMockAdapter implements ProveedorLogistica {

    private final RestClient restClient;

    public LogisticaMockAdapter(@Value("${services.logistica.mock.url:http://localhost:8088}") String baseUrl) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    /** DonaTrack manda un lote; el proveedor externo recibe de a un envío. */
    @Override
    public void registrarEntregas(DepositoRequest request) {
        for (EntregaRequest entrega : request.getEntregas()) {
            restClient.post()
                    .uri("/v1/shipments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(traducir(entrega))
                    .retrieve()
                    .toBodilessEntity();
        }
    }

    @Override
    public List<EntregaResponse> listarEntregas() {
        List<ShipmentResponse> envios = restClient.get()
                .uri("/v1/shipments")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ShipmentResponse>>() {});
        if (envios == null) {
            return Collections.emptyList();
        }
        return envios.stream().map(this::traducir).toList();
    }

    @Override
    public String nombre() {
        return "logistica externa mock";
    }

    private ShipmentRequest traducir(EntregaRequest entrega) {
        return new ShipmentRequest(entrega.getDonacionId(), entrega.getDireccionDestino());
    }

    private EntregaResponse traducir(ShipmentResponse envio) {
        EntregaResponse entrega = new EntregaResponse();
        entrega.setId(envio.getShipmentId());
        entrega.setDonacionId(envio.getExternalRef());
        entrega.setDireccionDestino(envio.getDestinationAddress());
        entrega.setEstado(traducirEstado(envio.getStatus()));
        entrega.setPatenteCamion(envio.getTruckPlate());
        entrega.setFechaHoraEntrega(envio.getDeliveredAt());
        entrega.setMotivoNoRecibida(envio.getFailureReason());
        return entrega;
    }

    /** Los estados resultantes tienen que coincidir con los que reconoce LogisticaPollingScheduler. */
    private String traducirEstado(String status) {
        if (status == null) {
            return "PENDIENTE";
        }
        return switch (status) {
            case "IN_TRANSIT" -> "EN_TRASLADO";
            case "DELIVERED" -> "ENTREGADA";
            case "FAILED" -> "NO_RECIBIDA";
            default -> "PENDIENTE";
        };
    }
}
