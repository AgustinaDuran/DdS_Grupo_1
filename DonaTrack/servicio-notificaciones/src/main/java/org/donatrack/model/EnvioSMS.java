package org.donatrack.model;

public class EnvioSMS extends MedioEnvio {

    public EnvioSMS(String nroTelefono) {
        this.destino = nroTelefono;
    }

    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        // Simulación del envío por SMS (integración real en próximas iteraciones).
        System.out.println("[SMS -> " + destino + "] " + nombreDestinatario + ": " + mensaje);
    }
}
