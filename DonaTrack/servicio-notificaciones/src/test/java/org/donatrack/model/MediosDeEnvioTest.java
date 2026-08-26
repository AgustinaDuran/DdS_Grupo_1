package org.donatrack.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import org.donatrack.service.email.ClienteEmail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Comportamiento de cada medio de envio.
 */
class MediosDeEnvioTest {

    /** Cliente de mail falso: registra lo que se le pidio enviar, sin salir a la red. */
    static class ClienteEmailFalso implements ClienteEmail {
        final List<String> enviados = new ArrayList<>();

        @Override
        public void enviar(String destino, String nombreDestinatario, String mensaje) {
            enviados.add(destino + "|" + nombreDestinatario + "|" + mensaje);
        }
    }

    @Test
    @DisplayName("EnvioMail delega en el cliente de email con destino, nombre y mensaje")
    void envioMailDelegaEnElCliente() {
        ClienteEmailFalso cliente = new ClienteEmailFalso();
        EnvioMail medio = new EnvioMail("juan@ejemplo.com", cliente);

        medio.notificar("Juan", "Tu donacion fue asignada.");

        assertThat(cliente.enviados)
                .containsExactly("juan@ejemplo.com|Juan|Tu donacion fue asignada.");
    }

    @Test
    @DisplayName("EnvioMail expone la direccion como destino")
    void envioMailGuardaElDestino() {
        EnvioMail medio = new EnvioMail("juan@ejemplo.com", new ClienteEmailFalso());

        assertThat(medio.getDestino()).isEqualTo("juan@ejemplo.com");
    }

    @Test
    @DisplayName("EnvioSMS todavia no esta implementado: falla en vez de simular exito")
    void envioSmsNoEstaImplementado() {
        EnvioSMS medio = new EnvioSMS("1155667788");

        assertThatThrownBy(() -> medio.notificar("Ana", "Hola"))
                .isInstanceOf(MedioNoSoportadoException.class)
                .hasMessageContaining("SMS");
    }

    @Test
    @DisplayName("EnvioWhatsapp todavia no esta implementado: falla en vez de simular exito")
    void envioWhatsappNoEstaImplementado() {
        EnvioWhatsapp medio = new EnvioWhatsapp("1199887766");

        assertThatThrownBy(() -> medio.notificar("Jose", "Hola"))
                .isInstanceOf(MedioNoSoportadoException.class)
                .hasMessageContaining("WHATSAPP");
    }
}
