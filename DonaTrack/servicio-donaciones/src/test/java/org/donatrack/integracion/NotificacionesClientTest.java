package org.donatrack.integracion;

import java.util.List;

import org.donatrack.integracion.dto.ContactoNotificacion;
import org.donatrack.integracion.dto.NotificacionRequest;
import org.donatrack.mensajeria.PublicadorRabbit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class NotificacionesClientTest {

    private NotificacionesClient cliente;
    private Object mensaje;

    @BeforeEach
    void prepararCliente() {
        cliente = new NotificacionesClient(new PublicadorRabbit(null) {
            @Override
            public void publicar(Object message) {
                NotificacionesClientTest.this.mensaje = message;
            }
        });
    }

    @Test
    void publicaDestinatarioMensajeYContactosEnRabbit() {
        cliente.enviar(new NotificacionRequest(7L, "Comedor Esperanza",
                "La donacion fue asignada.",
                List.of(new ContactoNotificacion("MAIL", "comedor@mail.com"))));

        assertThat(mensaje).isInstanceOf(NotificacionRequest.class);
        NotificacionRequest request = (NotificacionRequest) mensaje;
        assertThat(request.getNombreDestinatario()).isEqualTo("Comedor Esperanza");
        assertThat(request.getMensaje()).contains("asignada");
        assertThat(request.getContactos().get(0).getValor()).isEqualTo("comedor@mail.com");
    }
}
