package org.donatrack.service;
import org.donatrack.controller.dto.EntregaEntranteDTO;
import org.donatrack.controller.dto.CamionDTO;
import org.donatrack.controller.dto.SolicitudOptimizadorDTO;
import org.donatrack.model.*;
import org.donatrack.repository.CamionRepository;
import org.donatrack.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
 
@Service
public class LogisticaService {

    private final EntregaRepository entregaRepository;
    private final CamionRepository camionRepository;
    private final WebClient webClient;

    @Value("${optimizador.api.url:https://api.optimizador-externo.com/v1/planificar}")
    private String optimizadorUrl;

    @Value("${logistica.callback.url:https://tuservicio.donatrack.org/api/logistica/callback-planificador}")
    private String urlCallback;

    private static final int MAX_ENTREGAS_POR_LOTE = 100;

    public LogisticaService(EntregaRepository entregaRepository, CamionRepository camionRepository, WebClient.Builder webClientBuilder) {
        this.entregaRepository = entregaRepository;
        this.camionRepository = camionRepository;
        this.webClient = webClientBuilder.build();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void planificarRutasProgramado() {
        planificarRutasDelDia();
    }

//    public void registrarNuevasEntregas(List<Entrega> nuevasEntregas) {
//        entregaRepository.saveAll(nuevasEntregas);
//    }

    public void registrarNuevasEntregas(List<EntregaEntranteDTO> entregas) {
        List<Entrega> nuevas = entregas.stream()
                .map(dto -> new Entrega(dto.getDonacionId(), dto.getDireccionDestino()))
                .toList();
        entregaRepository.saveAll(nuevas);
    }

    public String planificarRutasDelDia() {
        List<Entrega> entregasPendientes = entregaRepository.findByEstado(EstadoEntrega.PENDIENTE);
        List<Camion> camionesDisponibles = camionRepository.findByRutaActivaIsNull();

        if (entregasPendientes.isEmpty()) {
            return "No hay entregas pendientes para planificar.";
        }
        if (camionesDisponibles.isEmpty()) {
            return "No hay camiones disponibles para planificar.";
        }

        List<Entrega> lote = entregasPendientes.size() > MAX_ENTREGAS_POR_LOTE
                ? entregasPendientes.subList(0, MAX_ENTREGAS_POR_LOTE)
                : entregasPendientes;

        SolicitudOptimizadorDTO solicitud = new SolicitudOptimizadorDTO(camionesDisponibles, lote, urlCallback);

        this.webClient.post()
                .uri(optimizadorUrl)
                .bodyValue(solicitud)
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                    response -> System.out.println("Petición enviada con éxito al optimizador externo."),
                    error -> System.err.println("Error al conectar con el optimizador: " + error.getMessage())
                );

        return "Solicitud de planificación enviada (" + lote.size() + " entregas, " + camionesDisponibles.size() + " camiones).";
    }

    public void procesarCallbackPlanificador(List<RutaReparto> rutas) {
        for (RutaReparto ruta : rutas) {
            Camion camion = camionRepository.findById(ruta.getPatenteCamion()).orElseThrow(() -> new RuntimeException("Camión no encontrado: " + ruta.getPatenteCamion()));
 
            if (camion.getRutaActiva() != null) {
                throw new IllegalStateException("El camión " + camion.getPatente() + " ya tiene una ruta activa.");
            }
 
            camion.setRutaActiva(ruta);
            camionRepository.save(camion);
        }
    }

    public void iniciarRuta(String patente) {
        Camion camion = camionRepository.findById(patente).orElseThrow(() -> new RuntimeException("Camión no encontrado: " + patente));
 
        RutaReparto ruta = camion.getRutaActiva();
        if (ruta == null) {
            throw new RuntimeException("El camión " + patente + " no tiene una ruta activa asignada.");
        }
        if (ruta.getFechaHoraInicio() != null) {
            throw new IllegalStateException("La ruta del camión " + patente + " ya fue iniciada.");
        }
        ruta.setFechaHoraInicio(LocalDateTime.now());
        for (Entrega entrega : ruta.getEntregas()) {
            entrega.asignarCamion(patente); //para llevar registro en el comprobante de entrega
            entrega.CambiarEstado(EstadoEntrega.EN_TRASLADO, null, null);
            entregaRepository.save(entrega);
        }
    }

    public void actualizarEstadoEntrega(Long id, EstadoEntrega nuevoEstado, String fotoUrl, String motivo) {
        Entrega entrega = entregaRepository.findById(id).orElseThrow(() -> new RuntimeException("Entrega no encontrada: " + id));
 
        entrega.CambiarEstado(nuevoEstado, fotoUrl, motivo);
        entregaRepository.save(entrega);
 
        if (entrega.EsEstadoTerminal()) {
            LiberarCamionSiRutaTerminada(entrega);
        }
    }

    private void LiberarCamionSiRutaTerminada(Entrega entrega) {
        Optional<Camion> camionOpt = camionRepository.findAll().stream()
                .filter(c -> c.getRutaActiva() != null && c.getRutaActiva().getEntregas().contains(entrega))
                .findFirst();
 
        camionOpt.ifPresent(camion -> {
            if (camion.getRutaActiva().EstaFinalizada()) {
                camion.setRutaActiva(null);
                camionRepository.save(camion);
            }
        });
    }

    //admin camiones
    public List<Camion> listarCamiones() {
        return camionRepository.findByActivoTrue();
    }
 
    public Camion crearCamion(CamionDTO dto) {
        if (camionRepository.existsById(dto.getPatente())) {
            throw new IllegalStateException("Ya existe un camión con patente: " + dto.getPatente());
        }
        Camion camion = new Camion(dto.getPatente(), dto.getCapacidadVolumen(), dto.getAltura(), dto.getCapacidadCarga());
        return camionRepository.save(camion);
    }
 
    public Camion modificarCamion(String patente, CamionDTO dto) {
        Camion camion = camionRepository.findById(patente).orElseThrow(() -> new RuntimeException("Camión no encontrado: " + patente));
 
        camion.setCapacidadVolumen(dto.getCapacidadVolumen());
        camion.setAltura(dto.getAltura());
        camion.setCapacidadCarga(dto.getCapacidadCarga());
        return camionRepository.save(camion);
    }
 
    public void darDeBajaCamion(String patente) {
        Camion camion = camionRepository.findById(patente).orElseThrow(() -> new RuntimeException("Camión no encontrado: " + patente));
 
        if (camion.getRutaActiva() != null) {
            throw new IllegalStateException("No se puede dar de baja el camión " + patente + " porque tiene una ruta activa.");
        }
 
        camion.setActivo(false);
        camionRepository.save(camion);
    }

    //seguimiento tiempo real
    public void actualizarUbicacionCamion(String patente, Double latitud, Double longitud) {
        Camion camion = camionRepository.findById(patente).orElseThrow(() -> new RuntimeException("Camión no encontrado: " + patente));
 
        camion.setLatitud(latitud);
        camion.setLongitud(longitud);
        camion.setUltimaActualizacionUbicacion(LocalDateTime.now());
        camionRepository.save(camion);
    }

    //para los otros servicios
    public Entrega ConsultarEntrega(Long id) {
        return entregaRepository.findById(id).orElseThrow(() -> new RuntimeException("Entrega no encontrada: " + id));
    }
 
    public RutaReparto ConsultarRutaActiva(String patente) {
        Camion camion = camionRepository.findById(patente).orElseThrow(() -> new RuntimeException("Camión no encontrado: " + patente));
        return camion.getRutaActiva();
    }

    public List<Entrega> listarEntregas(EstadoEntrega estado) {
        if (estado == null) {
            return entregaRepository.findAll();
        }
        return entregaRepository.findByEstado(estado);
    }

    public List<RutaReparto> listarRutasActivas() {
        return camionRepository.findByRutaActivaIsNotNull().stream()
                .map(Camion::getRutaActiva)
                .toList();
    }
}
