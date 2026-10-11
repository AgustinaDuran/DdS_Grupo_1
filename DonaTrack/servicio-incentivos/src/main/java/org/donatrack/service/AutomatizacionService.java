package org.donatrack.service;

import org.donatrack.model.Insignia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
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

    // Campos que recibe el escenario de make. 'clave' (ej. habil_donador_bronce) es la clave del
    // data store de insignias de make; 'mision' y 'nivel' son sus dos partes, en minúscula.
    static Map<String, Object> ArmarDatosInsignia(String nombre, String descripcionMision, Insignia insignia) {
        String clave = ClaveDe(insignia);
        int corte = clave.lastIndexOf('_');

        Map<String, Object> datos = new HashMap<>();
        datos.put("tipo", "INSIGNIA");
        datos.put("usuario", nombre);
        datos.put("insignia", insignia.GetNombre());
        datos.put("clave", clave);
        datos.put("mision", corte < 0 ? clave : clave.substring(0, corte));
        datos.put("nivel", corte < 0 ? "" : clave.substring(corte + 1));
        datos.put("descripcion", descripcionMision);
        datos.put("texto", "¡Felicitaciones a @" + nombre + "! Obtuvo la insignia " + insignia.GetNombre() + ".");
        datos.put("imagenUrl", insignia.GetImagen());
        return datos;
    }

    // "Hábil Donador - BRONCE" -> "habil_donador_bronce"
    static String ClaveDe(Insignia insignia) {
        String sinAcentos = Normalizer.normalize(insignia.GetNombre(), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinAcentos.toLowerCase(Locale.ROOT).replace(" - ", "_").replace(' ', '_');
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
