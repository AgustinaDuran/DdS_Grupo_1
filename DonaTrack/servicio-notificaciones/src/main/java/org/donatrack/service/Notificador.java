package org.donatrack.service;

import java.util.List;

import org.donatrack.controller.dto.CrearNotificacionDTO;
import org.donatrack.model.Contacto;
import org.donatrack.model.MedioEnvio;
import org.donatrack.model.Notificacion;
import org.donatrack.repository.NotificacionesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Notificador {

    private static final Logger log = LoggerFactory.getLogger(Notificador.class);

    private final NotificacionesRepository notificacionesRepository;
    private final MedioEnvioFactory medioEnvioFactory;

    public Notificador(NotificacionesRepository notificacionesRepository,
                       MedioEnvioFactory medioEnvioFactory) {
        this.notificacionesRepository = notificacionesRepository;
        this.medioEnvioFactory = medioEnvioFactory;
    }

    public Notificacion notificar(CrearNotificacionDTO crearNotificacionDTO) {

        List<Contacto> contactos = crearNotificacionDTO.getContactos();
        String nombreDestinatario = crearNotificacionDTO.getNombreDestinatario();

        // Los contactos vienen ordenados por prioridad (el predeterminado primero),
        // asi que se toma el primero que hoy pueda entregar el mensaje.
        Contacto contactoSeleccionado = contactos.stream()
                .filter(c -> medioEnvioFactory.soporta(c.getTipo()))
                .findFirst()
                .orElse(null);

        if (contactoSeleccionado == null) {
            return registrarSinMedioDisponible(crearNotificacionDTO, contactos);
        }

        MedioEnvio medio = medioEnvioFactory.crear(
                contactoSeleccionado.getTipo(), contactoSeleccionado.getValor());

        Notificacion notificacion = new Notificacion(
                contactoSeleccionado.getValor(),
                nombreDestinatario,
                crearNotificacionDTO.getMensaje(),
                medio);

        try {
            medio.notificar(nombreDestinatario, notificacion.getMensaje());
            notificacion.marcarComoEnviada();
            log.info("Notificacion {} enviada a '{}' por {}.",
                    notificacion.getId(), notificacion.getDestinatario(), contactoSeleccionado.getTipo());
        } catch (Exception e) {
            notificacion.marcarComoFallida(e.getMessage());
            log.error("Notificacion {} fallida hacia '{}': {}",
                    notificacion.getId(), notificacion.getDestinatario(), e.getMessage(), e);
        }

        this.notificacionesRepository.save(notificacion);
        return notificacion;
    }

    /**
     * Ningun contacto usa un medio implementado: se deja registro del intento
     * en lugar de descartarlo en silencio.
     */
    private Notificacion registrarSinMedioDisponible(CrearNotificacionDTO dto, List<Contacto> contactos) {
        String tipos = contactos.stream()
                .map(Contacto::getTipo)
                .reduce((a, b) -> a + ", " + b)
                .orElse("ninguno");

        String destino = contactos.isEmpty() ? "desconocido" : contactos.get(0).getValor();
        String motivo = "Ningun contacto usa un medio de envio implementado (recibidos: " + tipos + ").";

        Notificacion notificacion = new Notificacion(
                destino, dto.getNombreDestinatario(), dto.getMensaje(), null);
        notificacion.marcarComoFallida(motivo);

        log.warn("Notificacion {} no enviada a '{}': {}", notificacion.getId(), destino, motivo);

        this.notificacionesRepository.save(notificacion);
        return notificacion;
    }

    public List<Notificacion> obtenerNotificaciones() {
        return this.notificacionesRepository.findAll();
    }
}
