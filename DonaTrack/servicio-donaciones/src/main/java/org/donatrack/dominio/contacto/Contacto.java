package org.donatrack.dominio.contacto;

public class Contacto {
    // medio identifica el canal: MAIL, SMS o WHATSAPP
    private String medio;
    // valor es la dirección/número concreto (mail, teléfono, etc.)
    private String valor;

    public Contacto() {
    }

    public Contacto(String medio, String valor) {
        this.medio = medio;
        this.valor = valor;
    }

    public String getMedio() {
        return medio;
    }

    public void setMedio(String medio) {
        this.medio = medio;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
