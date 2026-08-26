package org.donatrack.config;

import org.donatrack.service.email.BrevoClienteEmail;
import org.donatrack.service.email.ClienteEmail;
import org.donatrack.service.email.ClienteEmailNoConfigurado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(EmailProperties.class)
public class EmailConfig {

    private static final Logger log = LoggerFactory.getLogger(EmailConfig.class);

    /**
     * Elige el cliente de mail segun haya o no credenciales.
     * Sin BREVO_API_KEY / BREVO_REMITENTE el servicio igual arranca, pero los
     * mails quedan en el log en vez de enviarse.
     */
    @Bean
    public ClienteEmail clienteEmail(EmailProperties propiedades, RestClient.Builder builder) {
        boolean hayCredenciales = StringUtils.hasText(propiedades.getApiKey())
                && StringUtils.hasText(propiedades.getRemitente());

        if (!hayCredenciales) {
            log.warn("BREVO_API_KEY y/o BREVO_REMITENTE no configurados. "
                    + "Los mails no se van a enviar y las notificaciones quedaran como FALLIDA.");
            return new ClienteEmailNoConfigurado();
        }

        log.info("Envio de mails habilitado via Brevo. Remitente: {}", propiedades.getRemitente());
        return new BrevoClienteEmail(propiedades, builder.build());
    }
}
