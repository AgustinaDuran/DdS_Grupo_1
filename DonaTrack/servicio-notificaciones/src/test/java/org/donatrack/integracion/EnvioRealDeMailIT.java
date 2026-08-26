package org.donatrack.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.donatrack.controller.dto.CrearNotificacionDTO;
import org.donatrack.model.Contacto;
import org.donatrack.model.EnumEstadoNotificacion;
import org.donatrack.model.Notificacion;
import org.donatrack.service.Notificador;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba de envio REAL contra Brevo. MANDA UN MAIL DE VERDAD.
 *
 * Arma el mismo pedido que envia servicio-donaciones (un destinatario con su
 * contacto de tipo MAIL) y lo pasa por el Notificador, asi recorre toda la
 * cadena: MedioEnvioFactory -> EnvioMail -> BrevoClienteEmail -> API de Brevo.
 *
 * Solo corre si estan las credenciales en el entorno; si no, se saltea, para que
 * un companero pueda clonar el repo y correr los tests sin cuenta de Brevo.
 *
 * Para ejecutarlo (PowerShell, desde servicio-notificaciones):
 *   $env:BREVO_API_KEY="xkeysib-..."
 *   $env:BREVO_REMITENTE="ftisman@frba.utn.edu.ar"
 *   mvn test -Dtest=EnvioRealDeMailIT
 */
@SpringBootTest
@EnabledIfEnvironmentVariable(named = "BREVO_API_KEY", matches = ".+")
class EnvioRealDeMailIT {

    /** Casilla que recibe la prueba. Cambiala si queres probar con otra. */
    private static final String MAIL_DEL_CLIENTE = "facutisman@gmail.com";
    private static final String NOMBRE_DEL_CLIENTE = "Facundo";

    @Autowired
    private Notificador notificador;

    @Test
    @DisplayName("Toma el mail del contacto del cliente y lo envia de verdad por Brevo")
    void enviaUnMailDeVerdadAlContactoDelCliente() {
        Notificacion resultado = notificador.notificar(pedidoDePrueba());

        assertThat(resultado.getMotivoFallo())
                .as("Brevo rechazo el envio")
                .isNull();
        assertThat(resultado.getEstado()).isEqualTo(EnumEstadoNotificacion.ENVIADA);
        assertThat(resultado.getDestinatario()).isEqualTo(MAIL_DEL_CLIENTE);

        System.out.println(">> Mail de prueba enviado a " + MAIL_DEL_CLIENTE + ". Revisa la bandeja (y spam).");
    }

    /** El mismo formato que arma DestinatarioResolver en servicio-donaciones. */
    private CrearNotificacionDTO pedidoDePrueba() {
        Contacto contacto = new Contacto();
        contacto.setTipo("MAIL");
        contacto.setValor(MAIL_DEL_CLIENTE);

        CrearNotificacionDTO pedido = new CrearNotificacionDTO();
        pedido.setDestinatarioId(1L);
        pedido.setNombreDestinatario(NOMBRE_DEL_CLIENTE);
        pedido.setContactos(List.of(contacto));
        pedido.setMensaje("Tu donacion fue asignada a una entidad beneficiaria. "
                + "Este mail lo genero el test de integracion de DonaTrack.");
        return pedido;
    }
}
