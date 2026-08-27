package org.donatrack.dominio.usuario.persona;
import org.donatrack.dominio.donante.DonantePersona;
import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.usuario.DatosUsuario;



import jakarta.persistence.*;

public class Persona extends DatosUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private Integer edad;
    private String dni;
    private String genero;
    private String direccion;
    private Contacto contactoPredeterminado;

    /** De un representante de organización sólo se conoce su nombre y cómo contactarlo. */
    public Persona(String nombre, String apellido, Contacto contactoPredeterminado) {
        this(nombre, apellido, null, null, null, null, contactoPredeterminado);
    }

    public Persona(String nombre, String apellido, Integer edad, String dni, String genero, String direccion, Contacto contactoPredeterminado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.dni = dni;
        this.genero = genero;
        this.direccion = direccion;
        this.contactoPredeterminado = contactoPredeterminado;

    }

    // --- GETTERS Y SETTERS ---

    public Long getId() {
        return id;
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

    public String getDireccion(){
        return direccion;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public Contacto getContactoPredeterminado() {
        return contactoPredeterminado;
    }

    public void setContactoPredeterminado(Contacto contactoPredeterminado) {
        this.contactoPredeterminado = contactoPredeterminado;
    }

}