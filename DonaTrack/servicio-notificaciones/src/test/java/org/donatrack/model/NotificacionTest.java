package org.donatrack.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotificacionTest {

    @Test
    @DisplayName("Una notificacion nace PENDIENTE y con fecha")
    void naceEnEstadoPendiente() {
        Notificacion notificacion = new Notificacion("a@b.com", "Ana", "Hola", null);

        assertThat(notificacion.getEstado()).isEqualTo(EnumEstadoNotificacion.PENDIENTE);
        assertThat(notificacion.getFechaEnvio()).isNotNull();
        assertThat(notificacion.getMotivoFallo()).isNull();
    }

    @Test
    @DisplayName("Marcarla como fallida guarda el motivo")
    void guardaElMotivoDelFallo() {
        Notificacion notificacion = new Notificacion("a@b.com", "Ana", "Hola", null);

        notificacion.marcarComoFallida("La API rechazo el envio");

        assertThat(notificacion.getEstado()).isEqualTo(EnumEstadoNotificacion.FALLIDA);
        assertThat(notificacion.getMotivoFallo()).isEqualTo("La API rechazo el envio");
    }

    @Test
    @DisplayName("Marcarla como enviada limpia el motivo de fallo previo")
    void reenvioExitosoLimpiaElMotivo() {
        Notificacion notificacion = new Notificacion("a@b.com", "Ana", "Hola", null);
        notificacion.marcarComoFallida("error transitorio");

        notificacion.marcarComoEnviada();

        assertThat(notificacion.getEstado()).isEqualTo(EnumEstadoNotificacion.ENVIADA);
        assertThat(notificacion.getMotivoFallo()).isNull();
    }

    @Test
    @DisplayName("Cada notificacion recibe un id distinto")
    void losIdsNoSeRepiten() {
        Notificacion una = new Notificacion("a@b.com", "Ana", "Hola", null);
        Notificacion otra = new Notificacion("c@d.com", "Jose", "Chau", null);

        assertThat(una.getId()).isNotEqualTo(otra.getId());
    }
}
