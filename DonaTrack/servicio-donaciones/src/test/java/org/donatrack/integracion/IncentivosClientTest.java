package org.donatrack.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.donatrack.integracion.dto.RegistrarDonacionRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncentivosClientTest {

    private HttpServer servidor;
    private String ruta;
    private String cuerpo;

    @BeforeEach
    void iniciarServidor() throws IOException {
        servidor = HttpServer.create(new InetSocketAddress(0), 0);
        servidor.createContext("/api/incentivos/registrar-donacion", this::recibir);
        servidor.setExecutor(Executors.newSingleThreadExecutor());
        servidor.start();
    }

    @AfterEach
    void detenerServidor() {
        servidor.stop(0);
    }

    @Test
    void enviaLaDonacionAlEndpointDeIncentivos() {
        IncentivosClient cliente = new IncentivosClient("http://localhost:" + servidor.getAddress().getPort());

        cliente.registrarDonacion("ana", new RegistrarDonacionRequest(
                "Arroz", List.of("Paquete"), null, java.time.LocalDate.of(2026, 8, 27)));

        assertThat(ruta).contains("/api/incentivos/registrar-donacion");
        assertThat(ruta).contains("nombreUsuario=ana");
        assertThat(cuerpo).contains("Arroz").contains("Paquete").contains("2026").contains("8").contains("27");
    }

    private void recibir(HttpExchange intercambio) throws IOException {
        ruta = intercambio.getRequestURI().toString();
        cuerpo = new String(intercambio.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        intercambio.sendResponseHeaders(204, -1);
        intercambio.close();
    }
}