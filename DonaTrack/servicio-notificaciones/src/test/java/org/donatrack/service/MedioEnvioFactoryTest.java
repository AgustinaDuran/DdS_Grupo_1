package org.donatrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.donatrack.model.EnvioMail;
import org.donatrack.model.EnvioSMS;
import org.donatrack.model.EnvioWhatsapp;
import org.donatrack.model.MedioNoSoportadoException;
import org.donatrack.service.email.ClienteEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MedioEnvioFactoryTest {

    private MedioEnvioFactory factory;

    @BeforeEach
    void inicializar() {
        ClienteEmail clienteQueNoHaceNada = (destino, nombre, mensaje) -> {
        };
        factory = new MedioEnvioFactory(clienteQueNoHaceNada);
    }

    @ParameterizedTest(name = "\"{0}\" se resuelve como envio por mail")
    @ValueSource(strings = {"MAIL", "mail", "  Mail  ", "EMAIL", "email"})
    @DisplayName("Acepta el tipo MAIL sin importar mayusculas ni espacios")
    void reconoceElTipoMail(String tipo) {
        assertThat(factory.soporta(tipo)).isTrue();
        assertThat(factory.crear(tipo, "a@b.com")).isInstanceOf(EnvioMail.class);
    }

    @Test
    @DisplayName("SMS y WhatsApp se construyen pero no estan soportados todavia")
    void construyeSmsYWhatsappPeroNoLosSoporta() {
        assertThat(factory.crear("SMS", "1155667788")).isInstanceOf(EnvioSMS.class);
        assertThat(factory.crear("WHATSAPP", "1155667788")).isInstanceOf(EnvioWhatsapp.class);

        assertThat(factory.soporta("SMS")).isFalse();
        assertThat(factory.soporta("WHATSAPP")).isFalse();
    }

    @Test
    @DisplayName("Un tipo desconocido no se soporta y no se puede construir")
    void rechazaTiposDesconocidos() {
        assertThat(factory.soporta("PALOMA_MENSAJERA")).isFalse();

        assertThatThrownBy(() -> factory.crear("PALOMA_MENSAJERA", "x"))
                .isInstanceOf(MedioNoSoportadoException.class);
    }

    @Test
    @DisplayName("Un tipo nulo no rompe: simplemente no se soporta")
    void toleraTipoNulo() {
        assertThat(factory.soporta(null)).isFalse();
    }
}
