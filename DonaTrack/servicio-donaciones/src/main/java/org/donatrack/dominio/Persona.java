package org.donatrack.dominio;

public class Persona{
    private String nombre;
    private String apellido;
    private Integer edad;
    private String dni;
    private String genero;
    private DonantePersona donantePersona;
    private RepresentanteOrganizacion representanteOrganizacion;

    public Persona(String nombre, String apellido, Integer edad, String dni, String genero, 
                   DonantePersona donantePersona, RepresentanteOrganizacion representanteOrganizacion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.dni = dni;
        this.genero = genero;
        this.donantePersona = donantePersona;
        this.representanteOrganizacion = representanteOrganizacion;
    }

    // --- GETTERS Y SETTERS ---

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

    public DonantePersona getDonantePersona() {
        return donantePersona;
    }

    public void setDonantePersona(DonantePersona DonantePersona) {
        this.donantePersona = donantePersona;
    }

    public RepresentanteOrganizacion getRepresentanteOrganizacion() {
        return representanteOrganizacion;
    }

    public void setRepresentanteOrganizacion(RepresentanteOrganizacion representanteOrganizacion) {
        this.representanteOrganizacion = representanteOrganizacion;
    }

}