package org.donatrack.controller.dto.Donantes;

import org.donatrack.dominio.donante.TipoPersonaJuridica;
import org.donatrack.dominio.usuario.RolUsuario;
import org.donatrack.dominio.contacto.Contacto;
import java.util.List;

public class CrearDonanteDTO {

    private TipoDonante tipo;

    // Campos Donante Juridico:
    private TipoPersonaJuridica tipoPersonaJuridica;
    private String rubro;

    private Long usuarioId;

    // Campos CrearUsuarioDTO
    private RolUsuario tipoUsuario;
    private List<ContactoDTO> contactos;


    // Campos Persona:
    private String nombre;
    private String apellido;
    private Integer edad;
    private String dni;
    private String genero;
    private String direccion;
    private ContactoDTO contactoPredeterminado;

    //Campos Organizacion:

    private String razonSocial;
    private String cuit;

    public CrearDonanteDTO() {
    }

    public TipoDonante getTipo() {
        return tipo;
    }
    public void setTipo(TipoDonante tipo) {
        this.tipo = tipo;
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
    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    public RolUsuario getTipoUsuario() {
        return tipoUsuario;
    }
    public void setTipoUsuario(RolUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
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
    public ContactoDTO getContactoDTOPredeterminado() {
        return contactoPredeterminado;
    }
    public void setContactoDTOPredeterminado(ContactoDTO contactoPredeterminado) {
        this.contactoPredeterminado = contactoPredeterminado;
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
    


}
