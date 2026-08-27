package org.donatrack.integracion;

import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.EstadoDonacion;
import org.donatrack.dominio.donacion.EstadoEnTraslado;
import org.donatrack.dominio.donacion.EstadoEntregaFallida;
import org.donatrack.dominio.donacion.EstadoEntregada;
import org.donatrack.dominio.donacion.TipoEstado;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.integracion.dto.ContactoNotificacion;
import org.donatrack.integracion.dto.EntregaResponse;
import org.donatrack.integracion.dto.NotificacionRequest;
import org.donatrack.repository.DonacionesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio de Donaciones — Notificaciones de Eventos por Logística (Entrega 3).
 *
 * Como Logística no puede invocar a Donaciones ni a Notificaciones, este componente hace
 * polling de las entregas de Logística y, al detectar un cambio respecto del estado local de
 * la donación, dispara la notificación correspondiente y sincroniza el estado local.
 */
@Component
public class LogisticaPollingScheduler {

    private static final Logger log = LoggerFactory.getLogger(LogisticaPollingScheduler.class);

    private final LogisticaClient logisticaClient;
    private final DonacionesRepository donacionesRepository;
    private final NotificacionesClient notificacionesClient;
    private final DestinatarioResolver destinatarioResolver;

    @Value("${logistica.mapa.url:http://localhost:8080/mapa}")
    private String mapaUrl;

    @Value("${services.admin.contacto.medio:MAIL}")
    private String adminMedio;

    @Value("${services.admin.contacto.valor:admin@donatrack.org}")
    private String adminValor;

    public LogisticaPollingScheduler(LogisticaClient logisticaClient, DonacionesRepository donacionesRepository,
                                     NotificacionesClient notificacionesClient, DestinatarioResolver destinatarioResolver) {
        this.logisticaClient = logisticaClient;
        this.donacionesRepository = donacionesRepository;
        this.notificacionesClient = notificacionesClient;
        this.destinatarioResolver = destinatarioResolver;
    }

    @Scheduled(fixedDelayString = "${logistica.polling.intervalo-ms:60000}")
    public void sincronizarEventosDeLogistica() {
        List<EntregaResponse> entregas = ultimaEntregaPorDonacion(logisticaClient.listarEntregas());

        for (EntregaResponse entrega : entregas) {
            try {
                procesarEntrega(entrega);
            } catch (Exception e) {
                log.warn("Error procesando la entrega {} de Logística: {}", entrega.getId(), e.getMessage());
            }
        }
    }

    /**
     * Una donación puede tener más de una entrega en Logística: si una queda no recibida y la
     * donación se replanifica, se crea otra. Sólo interesa el último intento.
     *
     * Comparar todas las entregas contra el único estado local de la donación haría que, ante
     * dos entregas en estados distintos, cada ciclo detecte una diferencia y vuelva a notificar
     * indefinidamente.
     *
     * Visible en el paquete para poder testear la selección sin levantar el ciclo completo.
     */
    List<EntregaResponse> ultimaEntregaPorDonacion(List<EntregaResponse> entregas) {
        Map<String, EntregaResponse> ultimas = new LinkedHashMap<>();
        for (EntregaResponse entrega : entregas) {
            if (entrega.getDonacionId() == null) {
                continue;
            }
            EntregaResponse previa = ultimas.get(entrega.getDonacionId());
            if (previa == null || esPosterior(entrega, previa)) {
                ultimas.put(entrega.getDonacionId(), entrega);
            }
        }
        return new ArrayList<>(ultimas.values());
    }

    /** El id autoincremental de Logística ordena los intentos: el mayor es el más reciente. */
    private boolean esPosterior(EntregaResponse candidata, EntregaResponse actual) {
        if (candidata.getId() == null) {
            return false;
        }
        return actual.getId() == null || candidata.getId() > actual.getId();
    }

    private void procesarEntrega(EntregaResponse entrega) {
        TipoEstado estadoLogistica = mapearEstado(entrega.getEstado());
        if (estadoLogistica == null) {
            return; // PENDIENTE u otros estados sin evento relevante
        }

        Long donacionId = parsearId(entrega.getDonacionId());
        if (donacionId == null) {
            return;
        }

        Donacion donacion = donacionesRepository.findById(donacionId);
        if (donacion == null) {
            return;
        }

        EstadoDonacion estadoActual = donacion.getEstadoDonacion();
        if (estadoActual != null && estadoActual.getEstado() == estadoLogistica) {
            return; // ya sincronizado / ya notificado
        }

        switch (estadoLogistica) {
            case EN_TRASLADO -> notificarInicioRuta(donacion, entrega);
            case ENTREGADA -> notificarEntregaExitosa(donacion, entrega);
            case ENTREGA_FALLIDA -> notificarEntregaFallida(donacion, entrega);
            default -> { return; }
        }

        sincronizarEstadoLocal(donacion, estadoLogistica, entrega);
    }

    private void notificarInicioRuta(Donacion donacion, EntregaResponse entrega) {
        String mensaje = "Tu entrega inició su recorrido. Seguila en tiempo real en el mapa: " + mapaUrl + "?camion=" + entrega.getPatenteCamion();
        EntidadBeneficiaria entidad = donacion.getEntidadAEntregar();
        if (entidad != null) {
            notificacionesClient.enviar(destinatarioResolver.paraEntidad(entidad, mensaje));
        }
        notificacionesClient.enviar(destinatarioResolver.paraDonante(donacion.getDonante(), mensaje));
    }

    private void notificarEntregaExitosa(Donacion donacion, EntregaResponse entrega) {
        String comprobante = "Comprobante de entrega — fecha/hora: "
        + (entrega.getFechaHoraEntrega() != null ? entrega.getFechaHoraEntrega() : "registrada")
        + ", camión responsable: " + entrega.getPatenteCamion() + ".";
        String mensaje = "La donación fue entregada correctamente. " + comprobante;
        EntidadBeneficiaria entidad = donacion.getEntidadAEntregar();
        if (entidad != null) {
            notificacionesClient.enviar(destinatarioResolver.paraEntidad(entidad, mensaje));
        }
        notificacionesClient.enviar(destinatarioResolver.paraDonante(donacion.getDonante(), mensaje));
    }

    private void notificarEntregaFallida(Donacion donacion, EntregaResponse entrega) {
        String motivo = entrega.getMotivoNoRecibida() != null ? entrega.getMotivoNoRecibida() : "motivo no especificado";
        String mensaje = "La entrega no pudo concretarse (" + motivo + "). El caso será revisado.";
        EntidadBeneficiaria entidad = donacion.getEntidadAEntregar();
        if (entidad != null) {
            notificacionesClient.enviar(destinatarioResolver.paraEntidad(entidad, mensaje));
        }
        notificacionesClient.enviar(destinatarioResolver.paraDonante(donacion.getDonante(), mensaje));
        notificacionesClient.enviar(notificacionAdministradores(mensaje));
    }

    private NotificacionRequest notificacionAdministradores(String mensaje) {
        return new NotificacionRequest(
                null,
                "Administración DonaTrack",
                mensaje,
                List.of(new ContactoNotificacion(adminMedio, adminValor)));
    }

    private void sincronizarEstadoLocal(Donacion donacion, TipoEstado estado, EntregaResponse entrega) {
        EntidadBeneficiaria entidad = donacion.getEntidadAEntregar();
        switch (estado) {
            case EN_TRASLADO -> donacion.setEstadoDonacion(new EstadoEnTraslado());
            case ENTREGADA -> donacion.setEstadoDonacion(new EstadoEntregada(entidad));
            case ENTREGA_FALLIDA -> donacion.setEstadoDonacion(new EstadoEntregaFallida(entidad, entrega.getMotivoNoRecibida()));
            default -> { }
        }
    }

    private TipoEstado mapearEstado(String estadoLogistica) {
        if (estadoLogistica == null) {
            return null;
        }
        return switch (estadoLogistica.toUpperCase()) {
            case "EN_TRASLADO" -> TipoEstado.EN_TRASLADO;
            case "ENTREGADA" -> TipoEstado.ENTREGADA;
            case "NO_RECIBIDA" -> TipoEstado.ENTREGA_FALLIDA;
            default -> null;
        };
    }

    private Long parsearId(String id) {
        try {
            return id != null ? Long.parseLong(id.trim()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
