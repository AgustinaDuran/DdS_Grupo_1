package org.donatrack.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.donatrack.controller.dto.CrearNotificacionDTO;
import org.donatrack.model.Contacto;
import org.donatrack.model.EnumEstadoNotificacion;
import org.donatrack.model.EnvioFallidoException;
import org.donatrack.model.Notificacion;
import org.donatrack.repository.NotificacionesRepository;
import org.donatrack.service.email.ClienteEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Estos son los mismos casos que se prueban a mano con curl contra el endpoint,
 * pero sin necesidad de levantar el servicio ni de tener credenciales de Brevo.
 */
class NotificadorTest {

    /** Cliente de mail falso: registra los envios o falla, segun se lo configure. */
    static class ClienteEmailFalso implements ClienteEmail {
        final List<String> destinos = new ArrayList<>();
        final List<String> nombres = new ArrayList<>();
        String errorAProducir;

        @Override
        public void enviar(String destino, String nombreDestinatario, String mensaje) {
            if (errorAProducir != null) {
                throw new EnvioFallidoException(errorAProducir, null);
            }
            destinos.add(destino);
            nombres.add(nombreDestinatario);
        }
    }

    private ClienteEmailFalso cliente;
    private NotificacionesRepository repositorio;
    private Notificador notificador;

    @BeforeEach
    void inicializar() {
        cliente = new ClienteEmailFalso();
        repositorio = new NotificacionesRepository();
        notificador = new Notificador(repositorio, new MedioEnvioFactory(cliente));
    }

    private Contacto contacto(String tipo, String valor) {
        Contacto c = new Contacto();
        c.setTipo(tipo);
        c.setValor(valor);
        return c;
    }

    private CrearNotificacionDTO pedido(String nombre, String mensaje, Contacto... contactos) {
        CrearNotificacionDTO dto = new CrearNotificacionDTO();
        dto.setNombreDestinatario(nombre);
        dto.setMensaje(mensaje);
        dto.setContactos(Arrays.asList(contactos));
        return dto;
    }

    @Test
    @DisplayName("Un contacto MAIL se envia y queda ENVIADA")
    void envioPorMailExitoso() {
        Notificacion resultado = notificador.notificar(
                pedido("Facundo", "Tu donacion fue asignada.", contacto("MAIL", "facundo@ejemplo.com")));

        assertThat(resultado.getEstado()).isEqualTo(EnumEstadoNotificacion.ENVIADA);
        assertThat(resultado.getMotivoFallo()).isNull();
        assertThat(cliente.destinos).containsExactly("facundo@ejemplo.com");
    }

    @Test
    @DisplayName("Al cliente de mail le llega el nombre, no la direccion (regresion)")
    void mandaElNombreYNoElMail() {
        // Antes se pasaba el valor del contacto como nombre, y el mail decia
        // "Hola facundo@ejemplo.com" en vez de "Hola Facundo".
        notificador.notificar(
                pedido("Facundo", "Hola", contacto("MAIL", "facundo@ejemplo.com")));

        assertThat(cliente.nombres).containsExactly("Facundo");
    }

    @Test
    @DisplayName("Si el unico contacto es SMS queda FALLIDA con el motivo")
    void sinMedioSoportadoQuedaFallida() {
        Notificacion resultado = notificador.notificar(
                pedido("Ana", "Hola", contacto("SMS", "1155667788")));

        assertThat(resultado.getEstado()).isEqualTo(EnumEstadoNotificacion.FALLIDA);
        assertThat(resultado.getMotivoFallo()).contains("SMS");
        assertThat(cliente.destinos).isEmpty();
    }

    @Test
    @DisplayName("Con WhatsApp primero y MAIL despues, usa el MAIL")
    void saltaLosMediosNoSoportados() {
        // Los contactos vienen ordenados por prioridad: si el predeterminado del
        // donante es WhatsApp, igual tiene que salir el mail.
        Notificacion resultado = notificador.notificar(pedido("Jose", "Ruta iniciada",
                contacto("WHATSAPP", "1199887766"),
                contacto("MAIL", "jose@ejemplo.com")));

        assertThat(resultado.getEstado()).isEqualTo(EnumEstadoNotificacion.ENVIADA);
        assertThat(resultado.getDestinatario()).isEqualTo("jose@ejemplo.com");
        assertThat(cliente.destinos).containsExactly("jose@ejemplo.com");
    }

    @Test
    @DisplayName("Con varios MAIL usa el primero, que es el predeterminado")
    void respetaLaPrioridadDeLosContactos() {
        Notificacion resultado = notificador.notificar(pedido("Jose", "Hola",
                contacto("MAIL", "principal@ejemplo.com"),
                contacto("MAIL", "secundario@ejemplo.com")));

        assertThat(resultado.getDestinatario()).isEqualTo("principal@ejemplo.com");
        assertThat(cliente.destinos).containsExactly("principal@ejemplo.com");
    }

    @Test
    @DisplayName("Si el proveedor rechaza el envio, queda FALLIDA con el motivo del proveedor")
    void falloDelProveedorQuedaRegistrado() {
        cliente.errorAProducir = "Brevo rechazo el envio (HTTP 401): Key not found";

        Notificacion resultado = notificador.notificar(
                pedido("Facundo", "Hola", contacto("MAIL", "facundo@ejemplo.com")));

        assertThat(resultado.getEstado()).isEqualTo(EnumEstadoNotificacion.FALLIDA);
        assertThat(resultado.getMotivoFallo()).contains("401");
    }

    @Test
    @DisplayName("Todo intento queda en el historial, haya salido o no")
    void todoQuedaEnElHistorial() {
        notificador.notificar(pedido("Facundo", "Hola", contacto("MAIL", "facundo@ejemplo.com")));
        notificador.notificar(pedido("Ana", "Hola", contacto("SMS", "1155667788")));

        assertThat(notificador.obtenerNotificaciones())
                .hasSize(2)
                .extracting(Notificacion::getEstado)
                .containsExactly(EnumEstadoNotificacion.ENVIADA, EnumEstadoNotificacion.FALLIDA);
    }
}
