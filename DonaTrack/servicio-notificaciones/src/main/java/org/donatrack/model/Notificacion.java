public class Notificacion{
    private Persona destinatario;
    private String mensaje;
    private Contacto medioEnvio;
    private LocalDate fechaEnvio;
    private Bool estado; 

    public Persona getDestinatario(){
        return destinatario;
    }

    public String getMensaje(){
        return mensaje;
    }

    public Contacto getMedioEnvio(){
        return medioEnvio;
    }

    public Bool getEstado(){
        return estado;
    }

    public Notificacion(Persona destinatario, String mensaje, MedioEnvioStrategy medio) {
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.medio = medio;
        this.estado = false;
        this.fechaCreacion = LocalDateTime.now();

    }

}