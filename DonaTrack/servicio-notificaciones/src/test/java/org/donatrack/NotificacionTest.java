package org.donatrack;

import org.donatrack.model.MedioEnvio;
import org.donatrack.model.Notificacion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica que enviarNotificacion() marque la notificación como enviada y
 * delegue el envío en el medio correspondiente con destinatario y mensaje.
 */
class NotificacionTest {

    /** MedioEnvio de prueba que registra la llamada a notificar(...). */
    static class MedioEnvioEspia extends MedioEnvio {
        boolean llamado = false;
        String ultimoDestinatario;
        String ultimoMensaje;

        @Override
        public void notificar(String nombreDestinatario, String mensaje) {
            this.llamado = true;
            this.ultimoDestinatario = nombreDestinatario;
            this.ultimoMensaje = mensaje;
        }
    }

    @Test
    void enviarNotificacionMarcaEstadoYDelegaEnElMedio() {
        MedioEnvioEspia espia = new MedioEnvioEspia();
        Notificacion notificacion = new Notificacion("Ana", "Tu donación fue asignada", espia);

        assertFalse(notificacion.getEstado());

        notificacion.enviarNotificacion();

        assertTrue(notificacion.getEstado());
        assertTrue(espia.llamado);
        assertEquals("Ana", espia.ultimoDestinatario);
        assertEquals("Tu donación fue asignada", espia.ultimoMensaje);
    }
}
