package org.donatrack.mensajeria;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableRabbit
public class ConfiguracionRabbit {

    public static final String EXCHANGE = "donatrack.notificaciones";
    public static final String QUEUE = "donatrack.notificaciones.queue";
    public static final String ROUTING_KEY = "notificacion";
    public static final String DEAD_LETTER_QUEUE = "donatrack.notificaciones.dlq";

    @Bean
    DirectExchange notificacionesExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    Queue notificacionesDeadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE, true);
    }

    @Bean
    Queue notificacionesQueue() {
        return new Queue(QUEUE, true, false, false, Map.of(
                "x-dead-letter-exchange", "",
                "x-dead-letter-routing-key", DEAD_LETTER_QUEUE));
    }

    @Bean
    Binding notificacionesBinding(Queue notificacionesQueue, DirectExchange notificacionesExchange) {
        return BindingBuilder.bind(notificacionesQueue)
                .to(notificacionesExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    Jackson2JsonMessageConverter rabbitMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                  Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
