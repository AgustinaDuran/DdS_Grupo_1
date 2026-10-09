package org.donatrack.integracion;

import org.donatrack.integracion.dto.NotificacionRequest;
import org.donatrack.mensajeria.PublicadorRabbit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificacionesClient {

    private static final Logger log = LoggerFactory.getLogger(NotificacionesClient.class);

    private final PublicadorRabbit publicador;

    public NotificacionesClient(PublicadorRabbit publicador) {
        this.publicador = publicador;
    }

    public void enviar(NotificacionRequest request) {
        if (request == null || request.getContactos() == null || request.getContactos().isEmpty()) {
            log.warn("No se notificó a '{}': no tiene contactos cargados.",
                    request != null ? request.getNombreDestinatario() : "desconocido");
            return;
        }
        try {
            publicador.publicar(request);
            log.info("Notificación de Incentivos encolada para '{}'", request.getNombreDestinatario());
        } catch (Exception e) {
            log.warn("No se pudo encolar la notificación de Incentivos para '{}': {}",
                    request.getNombreDestinatario(), e.getMessage());
        }
    }
}
