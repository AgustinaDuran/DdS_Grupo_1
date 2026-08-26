package org.donatrack.service;

import org.donatrack.model.EnvioMail;
import org.donatrack.model.EnvioSMS;
import org.donatrack.model.EnvioWhatsapp;
import org.donatrack.model.MedioEnvio;
import org.donatrack.model.MedioNoSoportadoException;
import org.donatrack.service.email.ClienteEmail;
import org.springframework.stereotype.Component;

/**
 * Construye el MedioEnvio que corresponde al tipo de contacto.
 * Existe como bean para poder inyectarle los colaboradores (el ClienteEmail)
 * a medios que no son beans de Spring.
 */
@Component
public class MedioEnvioFactory {

    private final ClienteEmail clienteEmail;

    public MedioEnvioFactory(ClienteEmail clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public MedioEnvio crear(String tipo, String valor) {
        return switch (normalizar(tipo)) {
            case "MAIL", "EMAIL" -> new EnvioMail(valor, clienteEmail);
            case "WHATSAPP" -> new EnvioWhatsapp(valor);
            case "SMS" -> new EnvioSMS(valor);
            default -> throw new MedioNoSoportadoException(tipo);
        };
    }

    /** Indica si el tipo de contacto puede entregar mensajes hoy. */
    public boolean soporta(String tipo) {
        String medio = normalizar(tipo);
        return medio.equals("MAIL") || medio.equals("EMAIL");
    }

    private String normalizar(String tipo) {
        return tipo == null ? "" : tipo.trim().toUpperCase();
    }
}
