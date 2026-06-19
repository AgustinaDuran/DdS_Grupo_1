package org.donatrack.model;

public class EnvioWhatsapp extends MedioEnvio {

    public EnvioWhatsapp(String nroTelefono) {
        this.destino = nroTelefono;
    }

    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        // Simulación del envío por WhatsApp (integración real en próximas iteraciones).
        System.out.println("[WHATSAPP -> " + destino + "] " + nombreDestinatario + ": " + mensaje);
    }
}
