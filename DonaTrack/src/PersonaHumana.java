public class PersonaHumana extends Donante{
    private String nombre;
    private String apellido;
    private int edad;
    private String nroDocumento;
    private String genero;
    private String direccion;
    private Contacto contactoPredeterminado;

    public PersonaHumana(String nombre, String apellido, int edad, String nroDocumento, String genero, String direccion, Mail mail) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nroDocumento = nroDocumento;
        this.edad = edad;
        this.direccion = direccion;
        this.genero = genero;
        this.contactos.add(mail);
        this.contactoPredeterminado = mail;
    }

    public PersonaHumana(String nombre,String apellido, String nroDocumento, Mail mail, Telefono telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nroDocumento = nroDocumento;
        this.contactos.add(mail);
        this.contactos.add(telefono);
        this.contactoPredeterminado = mail; 
    }

    public void setContactoPredeterminado(Contacto contacto){
        if(getContactos().contains(contacto)){
            throw new RuntimeException("El contacto no pertenece a la persona");
        }

        this.contactoPredeterminado = contacto;
    }
    public void cambiarContactoPreferido(Contacto contactoPreferidoNuevo){
        this.contactoPredeterminado=contactoPreferidoNuevo;
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

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
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

    public Contacto getContactoPredeterminado() {
        return contactoPredeterminado;
    }


}
