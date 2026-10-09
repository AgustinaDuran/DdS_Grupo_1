package org.donatrack.service.email;

/**
 * Abstraccion del proveedor de mails. Cambiar de proveedor (Brevo, SendGrid,
 * SMTP) es escribir otra implementacion, sin tocar el dominio.
 */
public interface ClienteEmail {

    /**
     * Envia el mensaje. Debe lanzar una excepcion si el envio no se concreta,
     * asi el Notificador puede registrar la notificacion como FALLIDA.
     */
    void enviar(String destino, String nombreDestinatario, String mensaje);
}
