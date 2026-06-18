package org.donatrack.model;

import java.time.LocalDateTime;
import org.donatrack.model.MedioEnvio;


public class Notificacion{
    private long id;
    private String destinatario;
    private String mensaje;
    private MedioEnvio medioEnvio;
    private LocalDate fechaEnvio;
    private Bool estado; 

    public Long getId(){
        return id;
    }

    public String getDestinatario(){
        return destinatario;
    }

    public String getMensaje(){
        return mensaje;
    }

    public MedioEnvio getMedioEnvio(){
        return medioEnvio;
    }

    public Bool getEstado(){
        return estado;
    }

    public Void setMensaje(mensaje){
        notificacion.mensaje = mensaje;
    }

    public Notificacion(String destinatario, String mensaje, MedioEnvio medio) {
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.medio = medio;
        this.estado = false;
        this.fechaCreacion = LocalDateTime.now();
    }

    public void enviarNotificacion(){
        this.estado = true;
        this.medioEnvio.notificar(this.destinatario, this.mensaje);
    }

}