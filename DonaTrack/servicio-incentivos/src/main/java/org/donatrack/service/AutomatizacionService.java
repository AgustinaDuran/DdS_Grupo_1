package org.donatrack.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class AutomatizacionService {

    private static final Logger log = LoggerFactory.getLogger(AutomatizacionService.class);

    /**
     * Con timeouts explícitos: un RestTemplate por defecto espera indefinidamente, así que si el
     * webhook de n8n acepta la conexión y no responde, el request que originó la notificación
     * queda colgado. La publicación en redes es accesoria y no debe frenar la donación.
     */
    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .readTimeout(Duration.ofSeconds(3))
            .build();

    @Value("${n8n.webhook.url}")
    private String n8nWebhookUrl;

    public void NotificarInsigniaGanada(String nombre, String descripcionMision, String urlImagenInsignia) { 
        Map<String, Object> datos = new HashMap<>(); //pal n8n
        datos.put("usuario", nombre);
        datos.put("texto", "¡Felicitaciones a @" + nombre + " por cumplir la misión: " + descripcionMision + "!");
        datos.put("imagenUrl", urlImagenInsignia);

        try {
            restTemplate.postForObject(n8nWebhookUrl, datos, String.class);
        } catch (Exception e) {
            log.warn("No se pudo publicar el hito en n8n: {}", e.getMessage());
        }
    }

    public void NotificarSubidaDeCategoria(String nombre, String nuevaCategoria) {
    Map<String, Object> datos = new HashMap<>();
    datos.put("usuario", nombre);
    datos.put("texto", "¡Felicitaciones a @" + nombre + " por ascender a la categoría " + nuevaCategoria + "!");

    try {
        restTemplate.postForObject(n8nWebhookUrl, datos, String.class);
    } catch (Exception e) {
        log.warn("No se pudo publicar el ascenso en n8n: {}", e.getMessage());
    }
}
}