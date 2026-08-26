package org.donatrack.controller;

import java.util.List;

import org.donatrack.controller.dto.CrearNotificacionDTO;
import org.donatrack.controller.dto.NotificacionResultadoDTO;
import org.donatrack.model.Notificacion;
import org.donatrack.service.Notificador;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificadorController {

    private final Notificador notificador;

    public NotificadorController(Notificador notificador) {
        this.notificador = notificador;
    }

    /**
     * Recibe un pedido de notificacion y lo procesa.
     * Devuelve 200 porque el pedido se recibio y quedo registrado; el resultado
     * real del envio (ENVIADA o FALLIDA) viaja en el cuerpo.
     */
    @PostMapping
    public ResponseEntity<NotificacionResultadoDTO> recibirNotificacion(
            @Valid @RequestBody CrearNotificacionDTO notificacionDTO) {

        Notificacion notificacion = notificador.notificar(notificacionDTO);

        return ResponseEntity.ok(new NotificacionResultadoDTO(notificacion));
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerHistorial() {
        return ResponseEntity.ok(notificador.obtenerNotificaciones());
    }
}
