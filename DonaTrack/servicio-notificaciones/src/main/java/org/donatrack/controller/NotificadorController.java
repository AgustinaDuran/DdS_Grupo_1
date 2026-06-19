package org.donatrack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.donatrack.controller.dto.CrearNotificacionDTO;
import org.donatrack.service.Notificador;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificadorController{
    private final Notificador notificador;

    public NotificadorController(Notificador notificador) {
        this.notificador = notificador;
    }

     //http://localhost:8080/api/notificaciones -> POST
    @PostMapping
    public ResponseEntity<String> recibirNotificacion(
        @RequestBody CrearNotificacionDTO notificacionDTO) {
        
        notificador.notificar(notificacionDTO);

        return ResponseEntity.ok("Notificacion enviada correctamente");
    }
}