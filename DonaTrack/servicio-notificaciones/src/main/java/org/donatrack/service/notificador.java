package org.donatrack.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.donatrack.controller.dto.*;

import java.util.List;



@Service
public class Notificador {

    private NotificacionesRepository notificacionesRepository;

    public void notificar(CrearNotificacionDTO crearNotificacionDTO) {
        
        List<Contacto> contactos = crearNotificacionDTO.getContactos();


        Contacto contactoSeleccionado = contactos.stream().findFirst().orElseThrow(() -> new RuntimeException("No se encontraron contactos"));
        //agarra el primer contacto, aca se aplicaria logica de elegir contacto si hubiera

        MedioEnvio medio= contactoSeleccionado.pasarAMedioEnvio();

        notificacion = new Notificacion(destinatario, mensaje, medio);
        
        // Enviamos inmediatamente la notificación, posible reforma a un job
        notificacion.enviarNotificacion();

        this.notificacionesRepository.save(notificacion);
        
    } 

    /*
    enviarNotificacion(){
        a tal hora/tal motivo. Se ejecuta 
    }*/ 
}

