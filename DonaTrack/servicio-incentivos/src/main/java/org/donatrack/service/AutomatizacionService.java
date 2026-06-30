package org.donatrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class AutomatizacionService {

    private final RestTemplate restTemplate = new RestTemplate();
    
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
            System.err.println("No se pudo conectar con n8n para publicar el hito: " + e.getMessage());
        }
    }
}