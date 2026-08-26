package org.donatrack.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.donatrack.controller.dto.CrearNotificacionDTO;
import org.donatrack.model.Notificacion;
import org.donatrack.service.Notificador;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

/**
 * Verifica el contrato HTTP que consume servicio-donaciones:
 * POST /api/notificaciones con {nombreDestinatario, mensaje, contactos:[{tipo, valor}]}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class NotificadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Notificador notificador;

    private static final String PEDIDO_VALIDO = """
            {"destinatarioId":1,"nombreDestinatario":"Facundo","mensaje":"Tu donacion fue asignada.",
             "contactos":[{"tipo":"MAIL","valor":"facundo@ejemplo.com"}]}
            """;

    @Test
    @DisplayName("Un pedido valido responde 200 con el estado real del envio")
    void pedidoValido() throws Exception {
        Notificacion enviada = new Notificacion("facundo@ejemplo.com", "Facundo", "Tu donacion fue asignada.", null);
        enviada.marcarComoEnviada();
        when(notificador.notificar(any(CrearNotificacionDTO.class))).thenReturn(enviada);

        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PEDIDO_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENVIADA"))
                .andExpect(jsonPath("$.destinatario").value("facundo@ejemplo.com"));
    }

    @Test
    @DisplayName("Un envio fallido tambien responde 200, pero informa FALLIDA y el motivo")
    void envioFallidoInformaElMotivo() throws Exception {
        Notificacion fallida = new Notificacion("1155667788", "Ana", "Hola", null);
        fallida.marcarComoFallida("Ningun contacto usa un medio de envio implementado (recibidos: SMS).");
        when(notificador.notificar(any(CrearNotificacionDTO.class))).thenReturn(fallida);

        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombreDestinatario":"Ana","mensaje":"Hola",
                                 "contactos":[{"tipo":"SMS","valor":"1155667788"}]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("FALLIDA"))
                .andExpect(jsonPath("$.motivoFallo").value(
                        "Ningun contacto usa un medio de envio implementado (recibidos: SMS)."));
    }

    @Test
    @DisplayName("Sin contactos responde 400 y no llega al servicio")
    void sinContactosEsPedidoInvalido() throws Exception {
        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombreDestinatario":"Facundo","mensaje":"Hola","contactos":[]}
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(notificador);
    }

    @Test
    @DisplayName("Con mensaje vacio responde 400 y no llega al servicio")
    void mensajeVacioEsPedidoInvalido() throws Exception {
        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombreDestinatario":"Facundo","mensaje":"",
                                 "contactos":[{"tipo":"MAIL","valor":"a@b.com"}]}
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(notificador);
    }

    @Test
    @DisplayName("El historial devuelve las notificaciones registradas")
    void devuelveElHistorial() throws Exception {
        Notificacion una = new Notificacion("facundo@ejemplo.com", "Facundo", "Hola", null);
        una.marcarComoEnviada();
        when(notificador.obtenerNotificaciones()).thenReturn(List.of(una));

        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].destinatario").value("facundo@ejemplo.com"))
                .andExpect(jsonPath("$[0].estado").value("ENVIADA"))
                .andExpect(jsonPath("$[0].fechaEnvio").exists());
    }
}
