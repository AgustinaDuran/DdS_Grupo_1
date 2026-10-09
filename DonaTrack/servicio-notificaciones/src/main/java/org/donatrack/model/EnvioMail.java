package org.donatrack.model;

import org.donatrack.service.email.ClienteEmail;

public class EnvioMail extends MedioEnvio {

    private final ClienteEmail clienteEmail;

    public EnvioMail(String direccionMail, ClienteEmail clienteEmail) {
        this.destino = direccionMail;
        this.clienteEmail = clienteEmail;
    }

    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        clienteEmail.enviar(this.destino, nombreDestinatario, mensaje);
    }
}
