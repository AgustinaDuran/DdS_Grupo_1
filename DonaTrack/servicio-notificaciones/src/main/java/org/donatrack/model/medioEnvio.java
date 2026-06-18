public abstract class MedioEnvio {
    private String destino;
    public void notificar(String nombreDestinatario, String mensaje);

}
public class EnvioWhatsapp extends MedioEnvio {
    public EnvioWhatsapp(String nroTelefono) {
        this.destino = nroTelefono;
    }
    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        //comunicacion con la api
    }
}

public class EnvioSMS extends MedioEnvio {

    public EnvioSMS(String nroTelefono) {
        this.destino = nroTelefono;
    }
    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        //comunicacion con la api


    }
}

public class EnvioMail extends MedioEnvio {
    

    public EnvioMail(String direccionMail) {
        this.destino = direccionMail;
    }
    
    @Override
    public void notificar(String nombreDestinatario, String mensaje) {
        //comunicacion con la api
    }

}