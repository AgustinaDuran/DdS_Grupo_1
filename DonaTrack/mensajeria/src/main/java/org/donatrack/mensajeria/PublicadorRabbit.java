package org.donatrack.mensajeria;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PublicadorRabbit {

    private final RabbitTemplate rabbitTemplate;

    public PublicadorRabbit(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicar(Object message) {
        rabbitTemplate.convertAndSend(
                ConfiguracionRabbit.EXCHANGE,
                ConfiguracionRabbit.ROUTING_KEY,
                message);
    }
}
