package org.donatrack.integracion.dto;

/**
 * Contacto en el formato que espera el Servicio de Notificaciones ({tipo, valor}).
 * tipo ∈ MAIL | SMS | WHATSAPP.
 */
public class ContactoNotificacion {
    private String tipo;
    private String valor;

    public ContactoNotificacion() {
    }

    public ContactoNotificacion(String tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
