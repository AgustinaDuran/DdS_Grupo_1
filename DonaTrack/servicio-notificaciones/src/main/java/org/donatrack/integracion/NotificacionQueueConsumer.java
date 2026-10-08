package org.donatrack.integracion;

import org.donatrack.controller.dto.CrearNotificacionDTO;
import org.donatrack.mensajeria.ConfiguracionRabbit;
import org.donatrack.service.Notificador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacionQueueConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificacionQueueConsumer.class);

    private final Notificador notificador;

    public NotificacionQueueConsumer(Notificador notificador) {
        this.notificador = notificador;
    }

    @RabbitListener(queues = ConfiguracionRabbit.QUEUE)
    public void consumir(CrearNotificacionDTO mensaje) {
        log.info("Notificación recibida desde RabbitMQ para '{}'", mensaje.getNombreDestinatario());
        notificador.notificar(mensaje);
    }
}
