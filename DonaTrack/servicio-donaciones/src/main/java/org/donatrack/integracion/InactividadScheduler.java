package org.donatrack.integracion;

import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.service.DonantesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

/**
 * Evento propio del Servicio de Donaciones (Entrega 2): notifica a las personas donantes que
 * no registran interacción (última donación) durante más de 20 días, para incentivarlas a
 * realizar una nueva donación.
 */
@Component
public class InactividadScheduler {

    private static final Logger log = LoggerFactory.getLogger(InactividadScheduler.class);
    private static final int DIAS_INACTIVIDAD = 20;

    private final DonantesService donantesService;
    private final NotificacionesClient notificacionesClient;
    private final DestinatarioResolver destinatarioResolver;

    public InactividadScheduler(DonantesService donantesService, NotificacionesClient notificacionesClient,
                                DestinatarioResolver destinatarioResolver) {
        this.donantesService = donantesService;
        this.notificacionesClient = notificacionesClient;
        this.destinatarioResolver = destinatarioResolver;
    }

    /** Se ejecuta a diario a las 09:00. */
    @Scheduled(cron = "${donaciones.inactividad.cron:0 0 9 * * ?}")
    public void notificarDonantesInactivos() {
        LocalDateTime limite = LocalDateTime.now().minusDays(DIAS_INACTIVIDAD);

        for (Donante donante : donantesService.obtenerDonantes()) {
            Optional<LocalDateTime> ultimaDonacion = donante.getDonacionesHistoricas().stream()
                    .map(Donacion::getFechaIngreso)
                    .filter(f -> f != null)
                    .max(Comparator.naturalOrder());

            if (ultimaDonacion.isPresent() && ultimaDonacion.get().isBefore(limite)) {
                notificacionesClient.enviar(destinatarioResolver.paraDonante(donante,
                        "Hace más de " + DIAS_INACTIVIDAD + " días que no realizás una donación. "
                                + "¡Te esperamos para seguir ayudando!"));
                log.info("Notificación de inactividad enviada al donante {}", donante.getId());
            }
        }
    }
}
