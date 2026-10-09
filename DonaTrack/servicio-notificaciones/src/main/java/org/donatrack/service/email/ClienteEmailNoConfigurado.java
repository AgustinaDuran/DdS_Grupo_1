package org.donatrack.service.email;

import org.donatrack.model.EnvioFallidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cliente de respaldo para cuando no hay credenciales de Brevo.
 * Permite que el servicio levante y se pueda probar sin una cuenta, pero
 * falla explicitamente: la notificacion queda FALLIDA con el motivo, en vez
 * de reportar un envio que nunca ocurrio. El mensaje igual se deja en el log
 * para poder verificar el flujo durante el desarrollo.
 */
public class ClienteEmailNoConfigurado implements ClienteEmail {

    private static final Logger log = LoggerFactory.getLogger(ClienteEmailNoConfigurado.class);

    @Override
    public void enviar(String destino, String nombreDestinatario, String mensaje) {
        log.warn("[MAIL NO ENVIADO] destino: {} | nombre: {} | mensaje: {}",
                destino, nombreDestinatario, mensaje);

        throw new EnvioFallidoException(
                "Envio de mails deshabilitado: falta configurar BREVO_API_KEY y BREVO_REMITENTE.", null);
    }
}
