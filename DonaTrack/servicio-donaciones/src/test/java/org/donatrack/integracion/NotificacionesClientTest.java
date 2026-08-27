package org.donatrack.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.donatrack.integracion.dto.ContactoNotificacion;
import org.donatrack.integracion.dto.NotificacionRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificacionesClientTest {

    private HttpServer servidor;
    private String ruta;
    private String cuerpo;

    @BeforeEach
    void iniciarServidor() throws IOException {
        servidor = HttpServer.create(new InetSocketAddress(0), 0);
        servidor.createContext("/api/notificaciones", this::recibir);
        servidor.setExecutor(Executors.newSingleThreadExecutor());
        servidor.start();
    }

    @AfterEach
    void detenerServidor() {
        servidor.stop(0);
    }

    @Test
    void enviaDestinatarioMensajeYContactosAlServicioDeNotificaciones() {
        NotificacionesClient cliente = new NotificacionesClient(
                "http://localhost:" + servidor.getAddress().getPort());

        cliente.enviar(new NotificacionRequest(7L, "Comedor Esperanza",
                "La donacion fue asignada.",
                List.of(new ContactoNotificacion("MAIL", "comedor@mail.com"))));

        assertThat(ruta).isEqualTo("/api/notificaciones");
        assertThat(cuerpo).contains("Comedor Esperanza")
                .contains("La donacion fue asignada.")
                .contains("comedor@mail.com")
                .contains("MAIL");
    }

    private void recibir(HttpExchange intercambio) throws IOException {
        ruta = intercambio.getRequestURI().toString();
        cuerpo = new String(intercambio.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        intercambio.sendResponseHeaders(204, -1);
        intercambio.close();
    }
}