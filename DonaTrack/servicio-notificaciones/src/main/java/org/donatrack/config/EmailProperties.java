package org.donatrack.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuracion del envio de mails.
 * Los valores se toman de application.properties, que a su vez los lee de
 * variables de entorno (BREVO_API_KEY, BREVO_REMITENTE, ...).
 */
@ConfigurationProperties(prefix = "notificaciones.email")
public class EmailProperties {

    private String apiUrl;
    private String apiKey;
    private String remitente;
    private String remitenteNombre;
    private String asunto;

    public String getApiUrl() { return apiUrl; }
    public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getRemitente() { return remitente; }
    public void setRemitente(String remitente) { this.remitente = remitente; }

    public String getRemitenteNombre() { return remitenteNombre; }
    public void setRemitenteNombre(String remitenteNombre) { this.remitenteNombre = remitenteNombre; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
}
