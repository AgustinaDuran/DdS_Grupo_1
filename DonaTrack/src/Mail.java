public class Mail extends Contacto {
    
    private String direccionMail;

    public Mail(String email) {
        direccionMail = email;
    }

    public String getDireccionMail() {
        return direccionMail;
    }

}
