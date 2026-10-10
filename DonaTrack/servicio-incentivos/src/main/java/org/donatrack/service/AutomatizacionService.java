package org.donatrack.service;

import org.donatrack.model.Insignia;
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
     * webhook de make acepta la conexión y no responde, el request que originó la notificación
     * queda colgado. La publicación en redes es accesoria y no debe frenar la donación.
     */
    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .readTimeout(Duration.ofSeconds(3))
            .build();

    @Value("${make.webhook.url}")
    private String makeWebhookUrl;

    public void NotificarInsigniaGanada(String nombre, String descripcionMision, Insignia insignia) {
        enviarAMake(ArmarDatosInsignia(nombre, descripcionMision, insignia), "el hito");
    }

    public void NotificarSubidaDeCategoria(String nombre, String nuevaCategoria) {
        enviarAMake(ArmarDatosAscenso(nombre, nuevaCategoria), "el ascenso");
    }

    // Campos que recibe el escenario de make. 'texto' ya viene armado; 'insignia', 'nivel' y
    // 'tipo' están sueltos para que el escenario pueda armar su propio mensaje o ramificar.
    static Map<String, Object> ArmarDatosInsignia(String nombre, String descripcionMision, Insignia insignia) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("tipo", "INSIGNIA");
        datos.put("usuario", nombre);
        datos.put("insignia", insignia.GetNombre());
        datos.put("nivel", String.valueOf(insignia.GetNivel()));
        datos.put("mision", descripcionMision);
        datos.put("texto", "¡Felicitaciones a @" + nombre + "! Obtuvo la insignia " + insignia.GetNombre() + ".");
        datos.put("imagenUrl", insignia.GetImagen());
        return datos;
    }

    static Map<String, Object> ArmarDatosAscenso(String nombre, String nuevaCategoria) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("tipo", "ASCENSO");
        datos.put("usuario", nombre);
        datos.put("categoria", nuevaCategoria);
        datos.put("texto", "¡Felicitaciones a @" + nombre + " por ascender a la categoría " + nuevaCategoria + "!");
        return datos;
    }

    private void enviarAMake(Map<String, Object> datos, String queEs) {
        try {
            restTemplate.postForObject(makeWebhookUrl, datos, String.class);
        } catch (Exception e) {
            log.warn("No se pudo publicar {} en make: {}", queEs, e.getMessage());
        }
    }
}
