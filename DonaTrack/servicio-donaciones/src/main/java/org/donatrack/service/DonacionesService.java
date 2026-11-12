package org.donatrack.service;

import org.springframework.stereotype.Service;
import org.donatrack.controller.dto.Donaciones.ActualizarDonacionDTO;
import org.donatrack.controller.dto.Donaciones.CrearDonacionDTO;
import org.donatrack.controller.dto.Donaciones.FiltrosDonacionDTO;
import org.donatrack.repository.DonacionesRepository;
import org.donatrack.repository.DonacionesCompletasRepository;
import org.donatrack.dominio.donante.*;
import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.entidadBeneficiaria.*;
import org.donatrack.integracion.DestinatarioResolver;
import org.donatrack.integracion.IncentivosClient;
import org.donatrack.integracion.broker.BrokerLogistica;
import org.donatrack.integracion.NotificacionesClient;
import org.donatrack.integracion.dto.DepositoRequest;
import org.donatrack.integracion.dto.EntregaRequest;
import org.donatrack.integracion.dto.RegistrarDonacionRequest;
import org.donatrack.repository.ResultadoAsignacionRepository;
import org.donatrack.dominio.algoritmo.ResultadoAsignacion;
import org.donatrack.service.excepciones.DonacionNoEncontradaException;
import org.donatrack.service.excepciones.RankingNoCalculadoException;
import org.donatrack.service.excepciones.TransicionEstadoInvalidaException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DonacionesService {
    private DonacionesRepository donacionesRepository;
    private DonacionesCompletasRepository donacionesCompletasRepository;
    private DonantesService donantesService;
    private EntidadesBeneficiariasService entidadesBeneficiariasService;
    private BienesService bienesService;
    private ResultadoAsignacionRepository resultadosRepository;

    private final NotificacionesClient notificacionesClient;
    private final IncentivosClient incentivosClient;
    private final BrokerLogistica brokerLogistica;
    private final DestinatarioResolver destinatarioResolver;

    public DonacionesService(DonacionesRepository donacionesRepository,
                             DonacionesCompletasRepository donacionesCompletasRepository,
                             DonantesService donantesService,
                             EntidadesBeneficiariasService entidadesBeneficiariasService, BienesService bienesService,
                             NotificacionesClient notificacionesClient, IncentivosClient incentivosClient,
                             BrokerLogistica brokerLogistica, DestinatarioResolver destinatarioResolver,
                             ResultadoAsignacionRepository resultadosRepository) {
        this.donacionesRepository = donacionesRepository;
        this.donacionesCompletasRepository = donacionesCompletasRepository;
        this.donantesService = donantesService;
        this.entidadesBeneficiariasService = entidadesBeneficiariasService;
        this.bienesService = bienesService;
        this.notificacionesClient = notificacionesClient;
        this.incentivosClient = incentivosClient;
        this.brokerLogistica = brokerLogistica;
        this.destinatarioResolver = destinatarioResolver;
        this.resultadosRepository = resultadosRepository;
    }

    public List<Donacion> obtenerDonaciones(FiltrosDonacionDTO filtrosDonacionDTO) {
        List<Donacion> donaciones = donacionesRepository.buscarConFiltros(
                filtrosDonacionDTO.getTipoEstado(),
                filtrosDonacionDTO.getFechaDesde(),
                filtrosDonacionDTO.getFechaHasta(),
                filtrosDonacionDTO.getSubcategoria());

        return donaciones;
    }

    public Donacion obtenerDonacionPorId(Long id) {
        return donacionesRepository.findById(id).orElse(null);
    }

    public List<Donacion> registrarDonacion(CrearDonacionDTO nuevaDonacion) {
                if (nuevaDonacion.getDonanteId() == null) {
            throw new IllegalArgumentException("Falta indicar el donante.");
        }
        Donante donante = this.donantesService.obtenerDonantePorId(nuevaDonacion.getDonanteId())
                .orElseThrow(() -> new IllegalArgumentException("El donante indicado no existe."));
        if (nuevaDonacion.getItems() == null || nuevaDonacion.getItems().isEmpty()) {
            throw new IllegalArgumentException("La donación debe tener al menos un bien.");
        }
        List<ItemBien> itemBienes = this.bienesService.convertirItemsDTOaItemsBien(nuevaDonacion.getItems());


        DonacionCompleta donacionCompleta = new DonacionCompleta(donante, itemBienes, nuevaDonacion.getDescripcion());
        donacionesCompletasRepository.save(donacionCompleta);

        List<Donacion> donacionesSegmentadas = donacionCompleta.segmentarDonacion();
        donacionesRepository.saveAll(donacionesSegmentadas);

        donacionesSegmentadas.forEach(d -> donante.agregarDonacionHistorica(d)); // de nuevo, puede que labure con ID's
                                                                                 // despues

        this.donantesService.guardarDonante(donante);

        // Avisar a Incentivos que el donante donó (impacta en el progreso de misiones).
        String nombreUsuario = destinatarioResolver.nombreUsuarioDe(donante);
        for (Donacion donacion : donacionesSegmentadas) {
            incentivosClient.registrarDonacion(nombreUsuario, construirRegistroIncentivos(donacion));
        }
        return donacionesSegmentadas;
    }

    public void eliminarDonacionPorId(Long id) {
        donacionesRepository.deleteById(id);
    }
    
    @Transactional
    public Donacion actualizarDonacion(Long id, ActualizarDonacionDTO datosActualizacion) {
        Donacion donacion = donacionesRepository.findById(id)
                .orElseThrow(() -> new DonacionNoEncontradaException("La donación no existe o fue eliminada."));

        if (datosActualizacion.getTipoEstado() != null) {
            donacion = this.cambiarEstadoDonacion(donacion, datosActualizacion);
        }
        if (datosActualizacion.getFotoEntrega() != null) {
            donacion.setFotoEntrega(datosActualizacion.getFotoEntrega());
        }

        return donacionesRepository.save(donacion);
    }

    private Donacion cambiarEstadoDonacion(Donacion donacion, ActualizarDonacionDTO datosActualizacion) {
        TipoEstado nuevoEstado = datosActualizacion.getTipoEstado();
        if (!donacion.esEstadoValido(nuevoEstado)) {
            throw new TransicionEstadoInvalidaException("Otra persona ya actualizó esta donación.");
        }

        switch (nuevoEstado) {
            case EN_DEPOSITO:
                donacion.siguiente();
                break;
            case ASIGNACION_REALIZADA:
                if (datosActualizacion.getEntidadId() == null) {
                    throw new IllegalArgumentException("Falta elegir la entidad.");
                }
                EntidadBeneficiaria entidad = entidadesBeneficiariasService.obtenerEntidadPorId(datosActualizacion.getEntidadId());
                if(entidad==null) {
                    throw new IllegalArgumentException("La entidad elegida no existe.");
                }
                ResultadoAsignacion resultado = resultadosRepository.findByDonacionId(donacion.getId())
                .orElseThrow(() -> new RankingNoCalculadoException("Esta donación todavía no tiene ranking."));

                resultado.confirmarDestinoFinal(entidad);
                resultadosRepository.delete(resultado);
                notificarAsignacion(donacion, entidad);
                enviarEntregaALogistica(donacion, entidad);
                break;
            case LISTA_PARA_ENTREGAR:
                donacion.siguiente();
                break;
            case EN_TRASLADO:
                // Los eventos de traslado/entrega los origina Logística y se detectan por
                // polling (LogisticaPollingScheduler); allí se disparan las notificaciones.
                donacion.siguiente();
                break;
            case ENTREGADA:
                donacion.siguiente();
                break;
            case ENTREGA_FALLIDA:
                donacion.falloEnEstado(datosActualizacion.getJustificacionEntregaFallida());
                break;
            case VENCIDA:
                donacion.falloEnEstado();
                resultadosRepository.deleteByDonacionId(donacion.getId());
                break;
            default:
                throw new IllegalArgumentException("Tipo de estado no válido");

        }


        return donacion;
    }

    private void notificarAsignacion(Donacion donacion, EntidadBeneficiaria entidad) {
        String descripcion = descripcionDonacion(donacion);
        notificacionesClient.enviar(destinatarioResolver.paraEntidad(entidad,
                "Se te asignó una donación (" + descripcion + ") en base a tus necesidades."));
        notificacionesClient.enviar(destinatarioResolver.paraDonante(donacion.getDonante(),
                "Tu donación (" + descripcion + ") fue asignada a una entidad beneficiaria."));
    }

    private void enviarEntregaALogistica(Donacion donacion, EntidadBeneficiaria entidad) {
        // Donaciones deja disponible la info de la entrega; Logística nunca llama a donaciones.
        EntregaRequest entrega = new EntregaRequest(String.valueOf(donacion.getId()), entidad.getDireccion());
        brokerLogistica.registrarEntregas(new DepositoRequest(List.of(entrega)));
    }

    private RegistrarDonacionRequest construirRegistroIncentivos(Donacion donacion) {
        String subcategoria = donacion.getSubcategoria() != null ? donacion.getSubcategoria().getNombre() : null;
        List<String> bienes = donacion.getItemBienes().stream()
                .map(item -> item.getBien().getNombre())
                .toList();
        return new RegistrarDonacionRequest(
                subcategoria,
                bienes,
                null,
                donacion.getFechaIngreso() != null ? donacion.getFechaIngreso().toLocalDate() : null,
                destinatarioResolver.paraDonante(donacion.getDonante(), "").getContactos());
    }

    private String descripcionDonacion(Donacion donacion) {
        if (donacion.getSubcategoria() != null) {
            return donacion.getSubcategoria().getNombre();
        }
        return donacion.getDescripcion() != null ? donacion.getDescripcion() : "donación";
    }

}
