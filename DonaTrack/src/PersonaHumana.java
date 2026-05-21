public class PersonaHumana extends Donante{
    private String nombre;
    private String apellido;
    private int edad;
    private int nroDocumento;
    private String genero;
    private String direccion;
    private Contacto contactoPredeterminado;

    public PersonaHumana() {
    }

    public void setContactoPredeterminado(Contacto contacto){
        if(getContactos().contains(contacto)){
            throw new RuntimeException("El contacto no pertenece a la persona");
        }

        this.contactoPredeterminado = contacto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNroDocumento(int nroDocumento) {
        this.nroDocumento = nroDocumento;
    }
}
