package org.donatrack.model;

public class EnvioMail extends MedioEnvio {

    public EnvioMail(String direccionMail) {
        this.destino = direccionMail;
    }

    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        // Simulación del envío por correo (integración real en próximas iteraciones).
        System.out.println("[MAIL -> " + destino + "] " + nombreDestinatario + ": " + mensaje);
    }
}
