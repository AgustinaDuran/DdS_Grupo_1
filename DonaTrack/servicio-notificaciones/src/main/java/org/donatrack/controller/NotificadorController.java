package org.donatrack.controller;

import org.donatrack.controller.dto.CrearNotificacionDTO;
import org.donatrack.model.Notificacion;
import org.donatrack.service.Notificador;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerHistorial() {
        return ResponseEntity.ok(notificador.obtenerNotificaciones());
    }
}


/*

private void enviarAlServicioDeNotificaciones(String nombre, List<Contacto> contactos, String mensaje) {
        CrearNotificacionDTO dto = new CrearNotificacionDTO();
        dto.setNombreDestinatario(nombre);
        dto.setMensaje(mensaje);
        dto.setContactos(contactos);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://localhost:8080/api/notificaciones", dto, String.class);
    }
}

public void registrarMisionCumplida(Donante donante, Mision mision) {
        donante.ganarInsignia(mision.getInsignia());

        // Llaman al método de abajo pasándole los datos en una sola línea
        enviarAlServicioDeNotificaciones(
            donante.getNombre(),
            donante.getContactos(),
            "¡Felicitaciones! Cumpliste la misión: " + mision.getNombre() //
        );
    }

* */