public abstract class MedioEnvio {

    public void enviar(Persona destinatario, String mensaje) {}
}

public class EnvioWhatsapp {
    public void enviar(Persona destinatario, String mensaje) {}
}

public class EnvioSMS{
    public void enviar(Persona destinatario, String mensaje) {}
}

public class EnvioMail{
    public void enviar(Persona destinatario, String mensaje) {}
}