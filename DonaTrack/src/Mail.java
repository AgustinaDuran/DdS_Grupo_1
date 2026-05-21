public class Mail extends Contacto {
    
    private String direccionMail;

    public Mail(String email) {
        super();
        direccionMail = email;
    }

    public String getDireccionMail() {
        return direccionMail;
    }

}
