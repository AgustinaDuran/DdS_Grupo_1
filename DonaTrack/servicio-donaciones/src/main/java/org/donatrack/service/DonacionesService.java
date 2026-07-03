package org.donatrack.service;

import org.springframework.stereotype.Service;
import org.donatrack.controller.dto.Donaciones.ActualizarDonacionDTO;
import org.donatrack.controller.dto.Donaciones.CrearDonacionDTO;
import org.donatrack.controller.dto.Donaciones.FiltrosDonacionDTO;
import org.donatrack.repository.DonacionesRepository;
import org.donatrack.dominio.donante.*;
import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.entidadBeneficiaria.*;
import org.donatrack.integracion.DestinatarioResolver;
import org.donatrack.integracion.IncentivosClient;
import org.donatrack.integracion.LogisticaClient;
import org.donatrack.integracion.NotificacionesClient;
import org.donatrack.integracion.dto.DepositoRequest;
import org.donatrack.integracion.dto.EntregaRequest;
import org.donatrack.integracion.dto.RegistrarDonacionRequest;

import java.util.List;

@Service
public class DonacionesService {
    private DonacionesRepository donacionesRepository;
    private DonantesService donantesService;
    private EntidadesBeneficiariasService entidadesBeneficiariasService;
    private BienesService bienesService;

    private final NotificacionesClient notificacionesClient;
    private final IncentivosClient incentivosClient;
    private final LogisticaClient logisticaClient;
    private final DestinatarioResolver destinatarioResolver;

    public DonacionesService(DonacionesRepository donacionesRepository, DonantesService donantesService,
                             EntidadesBeneficiariasService entidadesBeneficiariasService, BienesService bienesService,
                             NotificacionesClient notificacionesClient, IncentivosClient incentivosClient,
                             LogisticaClient logisticaClient, DestinatarioResolver destinatarioResolver) {
        this.donacionesRepository = donacionesRepository;
        this.donantesService = donantesService;
        this.entidadesBeneficiariasService = entidadesBeneficiariasService;
        this.bienesService = bienesService;
        this.notificacionesClient = notificacionesClient;
        this.incentivosClient = incentivosClient;
        this.logisticaClient = logisticaClient;
        this.destinatarioResolver = destinatarioResolver;
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
        return donacionesRepository.findById(id);
    }

    public void registrarDonacion(CrearDonacionDTO nuevaDonacion) {
        Donante donante = this.donantesService.obtenerDonantePorId(nuevaDonacion.getDonanteId()).orElseThrow();
        List<ItemBien> itemBienes = this.bienesService.convertirItemsDTOaItemsBien(nuevaDonacion.getItems());


        DonacionCompleta donacionCompleta = new DonacionCompleta(donante, itemBienes, nuevaDonacion.getDescripcion());

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
    }

    public void eliminarDonacionPorId(Long id) {
        donacionesRepository.delete(id);
    }

    public void actualizarDonacion(Long id, ActualizarDonacionDTO datosActualizacion) {
        Donacion donacion = donacionesRepository.findById(id);

        if (donacion == null) {
            throw new IllegalArgumentException("No se encontró la donación con el ID proporcionado");
        }
        if (datosActualizacion.getTipoEstado() != null) {
            donacion = this.cambiarEstadoDonacion(donacion, datosActualizacion);
        }
        if (datosActualizacion.getFotoEntrega() != null) {
            donacion.setFotoEntrega(datosActualizacion.getFotoEntrega());
        }

        donacionesRepository.save(donacion);

    }

    private Donacion cambiarEstadoDonacion(Donacion donacion, ActualizarDonacionDTO datosActualizacion) {
        TipoEstado nuevoEstado = datosActualizacion.getTipoEstado();
        if (!donacion.esEstadoValido(nuevoEstado)) {
            throw new Error("La donación no puede ser pasada a este estado desde el que está");
        }

        switch (nuevoEstado) {
            case EN_DEPOSITO:
                donacion.siguiente();
                break;
            case ASIGNACION_REALIZADA:
                EntidadBeneficiaria entidad = entidadesBeneficiariasService.obtenerEntidadPorId(datosActualizacion.getEntidadId());
                donacion.siguiente(entidad);
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
        logisticaClient.registrarEntregas(new DepositoRequest(List.of(entrega)));
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
                donacion.getFechaIngreso() != null ? donacion.getFechaIngreso().toLocalDate() : null);
    }

    private String descripcionDonacion(Donacion donacion) {
        if (donacion.getSubcategoria() != null) {
            return donacion.getSubcategoria().getNombre();
        }
        return donacion.getDescripcion() != null ? donacion.getDescripcion() : "donación";
    }

}
