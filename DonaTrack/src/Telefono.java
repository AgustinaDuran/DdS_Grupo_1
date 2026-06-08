public class Telefono extends Contacto {
    
    private String nroTelefono;

    public Telefono(String telefono) {
        this.nroTelefono= telefono;
    }

    public String getNroTelefono(){
        return nroTelefono;
    }
}
