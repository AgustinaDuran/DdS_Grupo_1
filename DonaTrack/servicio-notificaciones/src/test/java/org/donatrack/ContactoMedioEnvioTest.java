package org.donatrack;

import org.donatrack.model.EnvioMail;
import org.donatrack.model.EnvioSMS;
import org.donatrack.model.EnvioWhatsapp;
import org.donatrack.model.MedioEnvio;
import org.donatrack.model.contacto.Mail;
import org.donatrack.model.contacto.Telefono;
import org.donatrack.model.contacto.Whatsapp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Verifica que cada tipo de contacto se traduzca al medio de envío correcto.
 */
class ContactoMedioEnvioTest {

    @Test
    void mailSeTraduceAEnvioMail() {
        MedioEnvio medio = new Mail("ana@mail.com").pasarAMedioEnvio();
        assertInstanceOf(EnvioMail.class, medio);
    }

    @Test
    void telefonoSeTraduceAEnvioSMS() {
        MedioEnvio medio = new Telefono("+54 11 5555-5555").pasarAMedioEnvio();
        assertInstanceOf(EnvioSMS.class, medio);
    }

    @Test
    void whatsappSeTraduceAEnvioWhatsapp() {
        MedioEnvio medio = new Whatsapp("+54 11 4444-4444").pasarAMedioEnvio();
        assertInstanceOf(EnvioWhatsapp.class, medio);
    }
}
