package org.donatrack.controller.dto.Donantes;

public class ContactoDTO {
    private String medio;
    private String valor;
    public ContactoDTO() {
    }
    public ContactoDTO(String medio, String valor) {
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
