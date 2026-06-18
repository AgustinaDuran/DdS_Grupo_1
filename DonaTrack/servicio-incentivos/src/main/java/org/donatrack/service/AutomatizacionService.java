package org.donatrack.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class AutomatizacionService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    private final String N8N_WEBHOOK_INSIGNIA_URL = "http://localhost:5678/webhook-test/insignia-ganada";

    public void notificarInsigniaGanada(String nombreUsuario, String descripcionMision, String urlImagenInsignia) { 
        Map<String, Object> datos = new HashMap<>(); //pal n8n
        datos.put("usuario", nombreUsuario);
        datos.put("texto", "¡Felicitaciones a @" + nombreUsuario + " por cumplir la misión: " + descripcionMision + "!");
        datos.put("imagenUrl", urlImagenInsignia);

        try {
            restTemplate.postForObject(N8N_WEBHOOK_INSIGNIA_URL, datos, String.class);
        } catch (Exception e) {
            System.err.println("No se pudo conectar con n8n para publicar el hito: " + e.getMessage());
        }
    }
}