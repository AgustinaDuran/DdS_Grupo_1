public class Notificador {

    public static Notificador instancia;

    private Notificador() {}

    public static Notificador getNotificador() {
        if(instancia == null) {
            instancia = new Notificador(); 
        }
        return instancia;
    }

    public void notificar(Notificacion notificacion) {
        Persona destinatario = notificacion.getPersona();
        String mensaje = notificacion.getMensaje();

        notificacion.getMedioEnvio().notificar(destinatario, mensaje);
    } 
}