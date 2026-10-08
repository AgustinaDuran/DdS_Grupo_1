package org.donatrack.integracion;

import org.donatrack.integracion.dto.NotificacionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.donatrack.mensajeria.PublicadorRabbit;
import org.springframework.stereotype.Component;

@Component
public class NotificacionesClient {

    private static final Logger log = LoggerFactory.getLogger(NotificacionesClient.class);

    private final PublicadorRabbit publicador;

    public NotificacionesClient(PublicadorRabbit publicador) {
        this.publicador = publicador;
    }

    public void enviar(NotificacionRequest request) {
        if (request.getContactos() == null || request.getContactos().isEmpty()) {
            log.warn("No se notificó a '{}': no tiene ningún contacto cargado.",
                    request.getNombreDestinatario());
            return;
        }
        try {
            publicador.publicar(request);
            log.info("Notificación encolada para '{}'", request.getNombreDestinatario());
        } catch (Exception e) {
            log.warn("No se pudo encolar la notificación a '{}': {}",
                    request.getNombreDestinatario(), e.getMessage());
        }
    }
}
