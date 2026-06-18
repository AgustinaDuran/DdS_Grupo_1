package org.donatrack.model.contacto;

public abstract class Contacto {
    private String destino;
    //por si se necesitan implementar metodos
    public MedioEnvio pasarAMedioEnvio();
}

public class Mail extends Contacto {
    
    

    public Mail(String email) {
        this.destino = email;
    }

    public String getDireccionMail() {
        return destino;
    }

    @Override 
    public MedioEnvio pasarAMedioEnvio(){
        MedioEnvio medio = new EnvioMail(this.direccionMail);
        return medio;
    }
}

public class Telefono extends Contacto {

    public Telefono(String telefono) {
        this.destino = telefono;
    }

    public String getNroTelefono(){
        return destino;
    }
    @Override 
    public MedioEnvio pasarAMedioEnvio(){
        MedioEnvio medio = new EnvioSMS(this.destino);
        return medio;
    }
}

public class Whatsapp extends Contacto{
    

    public Whatsapp(String whatsapp){
        this.destino = whatsapp;
    }

    public String getNroTelefono(){
        return destino;
    }

    @Override 
    public MedioEnvio pasarAMedioEnvio(){
        MedioEnvio medio = new EnvioWhatsapp(this.destino);
        return medio;
    }

}