package org.donatrack.controller.dto.Usuarios;

import org.donatrack.controller.dto.Donantes.ContactoDTO;
import org.donatrack.controller.dto.Donantes.CrearDonanteDTO;
import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.usuario.RolUsuario;

public class CrearUsuarioDTO {
    

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

    
    private RolUsuario rolUsuario;

    public CrearUsuarioDTO(CrearDonanteDTO crearDonanteDTO) {
        System.out.println("Creando CrearUsuarioDTO a partir de CrearDonanteDTO: " + crearDonanteDTO);
        this.rolUsuario = crearDonanteDTO.getTipoUsuario();

        if (rolUsuario == RolUsuario.PERSONA) {
            this.nombre = crearDonanteDTO.getNombre();
            this.apellido = crearDonanteDTO.getApellido();
            this.edad = crearDonanteDTO.getEdad();
            this.dni = crearDonanteDTO.getDni();
            this.genero = crearDonanteDTO.getGenero();
            this.direccion = crearDonanteDTO.getDireccion();
            this.contactoPredeterminado = crearDonanteDTO.getContactoDTOPredeterminado();
        } else {
            this.razonSocial = crearDonanteDTO.getRazonSocial();
            this.cuit = crearDonanteDTO.getCuit();
        }
    
    }

    public CrearUsuarioDTO() {
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
    public RolUsuario getRolUsuario() {
        return rolUsuario;
    }
    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

}
