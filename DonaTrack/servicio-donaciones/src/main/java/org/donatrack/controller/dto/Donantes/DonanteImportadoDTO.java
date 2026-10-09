package org.donatrack.controller.dto.Donantes;

/**
 * Representa una fila ya parseada del CSV de importación masiva de personas donantes.
 * Campos mínimos según la consigna: TipoPersona, TipoDoc, Documento, Nombre/Razón Social,
 * Email, Teléfono.
 */
public class DonanteImportadoDTO {

    private TipoDonante tipoPersona;
    private String tipoDoc;
    private String documento;
    private String nombreRazonSocial;
    private String email;
    private String telefono;

    public DonanteImportadoDTO() {
    }

    public DonanteImportadoDTO(TipoDonante tipoPersona, String tipoDoc, String documento,
                               String nombreRazonSocial, String email, String telefono) {
        this.tipoPersona = tipoPersona;
        this.tipoDoc = tipoDoc;
        this.documento = documento;
        this.nombreRazonSocial = nombreRazonSocial;
        this.email = email;
        this.telefono = telefono;
    }

    public TipoDonante getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoDonante tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombreRazonSocial() {
        return nombreRazonSocial;
    }

    public void setNombreRazonSocial(String nombreRazonSocial) {
        this.nombreRazonSocial = nombreRazonSocial;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
