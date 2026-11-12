package org.donatrack.controller.dto.Donantes;

import java.util.List;

import org.donatrack.dominio.donante.TipoPersonaJuridica;

public class ActualizarDonanteDTO {

    private String nombre;
    private String apellido;
    private Integer edad;
    private String dni;
    private String genero;
    private String direccion;
    private ContactoDTO contactoPredeterminado;

    private TipoPersonaJuridica tipoPersonaJuridica;
    private String rubro;
    private String razonSocial;
    private String cuit;
    private List<ContactoDTO> contactos;

    public ActualizarDonanteDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public ContactoDTO getContactoPredeterminado() {
        return contactoPredeterminado;
    }

    public void setContactoPredeterminado(ContactoDTO contactoPredeterminado) {
        this.contactoPredeterminado = contactoPredeterminado;
    }

    public TipoPersonaJuridica getTipoPersonaJuridica() {
        return tipoPersonaJuridica;
    }

    public void setTipoPersonaJuridica(TipoPersonaJuridica tipoPersonaJuridica) {
        this.tipoPersonaJuridica = tipoPersonaJuridica;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public List<ContactoDTO> getContactos() {
        return contactos;
    }

    public void setContactos(List<ContactoDTO> contactos) {
        this.contactos = contactos;
    }
}
