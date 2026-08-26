package org.donatrack.service.email;

import java.util.List;
import java.util.Map;

import org.donatrack.config.EmailProperties;
import org.donatrack.model.EnvioFallidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

/**
 * Envia mails a traves de la API transaccional de Brevo.
 * POST https://api.brevo.com/v3/smtp/email con el header "api-key".
 */
public class BrevoClienteEmail implements ClienteEmail {

    private static final Logger log = LoggerFactory.getLogger(BrevoClienteEmail.class);

    private final EmailProperties propiedades;
    private final RestClient restClient;

    public BrevoClienteEmail(EmailProperties propiedades, RestClient restClient) {
        this.propiedades = propiedades;
        this.restClient = restClient;
    }

    @Override
    public void enviar(String destino, String nombreDestinatario, String mensaje) {
        Map<String, Object> cuerpo = Map.of(
                "sender", Map.of(
                        "name", propiedades.getRemitenteNombre(),
                        "email", propiedades.getRemitente()),
                "to", List.of(Map.of(
                        "email", destino,
                        "name", nombreDestinatario == null ? destino : nombreDestinatario)),
                "subject", propiedades.getAsunto(),
                "textContent", mensaje,
                "htmlContent", armarHtml(nombreDestinatario, mensaje));

        try {
            restClient.post()
                    .uri(propiedades.getApiUrl())
                    .header("api-key", propiedades.getApiKey())
                    .header("accept", MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cuerpo)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Mail enviado a '{}' via Brevo.", destino);

        } catch (RestClientResponseException e) {
            // Brevo devuelve el motivo en el cuerpo: unauthorized, sender not valid, etc.
            throw new EnvioFallidoException(
                    "Brevo rechazo el envio a '" + destino + "' (HTTP " + e.getStatusCode().value()
                            + "): " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new EnvioFallidoException(
                    "No se pudo contactar a Brevo para enviar a '" + destino + "': " + e.getMessage(), e);
        }
    }

    private String armarHtml(String nombreDestinatario, String mensaje) {
        String saludo = (nombreDestinatario == null || nombreDestinatario.isBlank())
                ? "Hola,"
                : "Hola " + escapar(nombreDestinatario) + ",";

        return """
                <html><body style="font-family: Arial, Helvetica, sans-serif; color: #333;">
                  <p>%s</p>
                  <p>%s</p>
                  <hr style="border:none;border-top:1px solid #ddd;">
                  <p style="font-size:12px;color:#888;">Este es un mensaje automatico de DonaTrack.</p>
                </body></html>
                """.formatted(saludo, escapar(mensaje).replace("\n", "<br>"));
    }

    private String escapar(String texto) {
        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
