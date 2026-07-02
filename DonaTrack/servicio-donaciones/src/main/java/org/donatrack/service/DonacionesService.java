package org.donatrack.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.donatrack.controller.dto.*;
import org.donatrack.controller.dto.Donaciones.ActualizarDonacionDTO;
import org.donatrack.controller.dto.Donaciones.CrearDonacionDTO;
import org.donatrack.controller.dto.Donaciones.FiltrosDonacionDTO;
import org.donatrack.repository.DonacionesRepository;
import org.donatrack.dominio.donante.*;
import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.entidadBeneficiaria.*;

import java.util.ArrayList;
import java.util.List;

//quiza esto se mueve a dependencies / inyections (archivo que inicialize todo)

@Service
public class DonacionesService {
    private DonacionesRepository donacionesRepository;
    private DonantesService donantesService;
    private EntidadesBeneficiariasService entidadesBeneficiariasService;
    private BienesService bienesService;
    private final RestClient restClient;

    public DonacionesService(DonacionesRepository donacionesRepository, DonantesService donantesService,
                             EntidadesBeneficiariasService entidadesBeneficiariasService, BienesService bienesService) {
        this.donacionesRepository = donacionesRepository;
        this.donantesService = donantesService;
        this.entidadesBeneficiariasService = entidadesBeneficiariasService;
        this.bienesService = bienesService;
        this.restClient = RestClient.create("http://localhost:8081");
    } // o seteado post instanciacion en archivo dependencias

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

        //avisar a incentivos de que un donanto donó

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

        switch (nuevoEstado) { //aca se mandan las notificaciones
            case EN_DEPOSITO:
                donacion.siguiente();
                break;
            case ASIGNACION_REALIZADA:
                EntidadBeneficiaria entidad = entidadesBeneficiariasService.obtenerEntidadPorId(datosActualizacion.getEntidadId());
                donacion.siguiente(entidad);
                //notificacion a entidad que se le asignó
                //notificacion a donante que se asigno una donacion suya
                //actualizar necesidades de entidad? para no asignarle a algo ya satisfecho?
                break;
            case LISTA_PARA_ENTREGAR:
                donacion.siguiente();

                break;
            case EN_TRASLADO:
                donacion.siguiente();
                //notificacion a entidad que su donacion esta en camino
                //notificacion a donante que su donacion esta en camino
                break;
            case ENTREGADA:
                donacion.siguiente();
                //notificacion a entidad que acepto
                //notificacion a donante que envio
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

    /* public void asignarDonacion(EntidadBeneficiaria entidadBeneficiaria, Donacion donacion) {
        donacion.asignar(entidadBeneficiaria);
        
        CrearDonacionDTO notificacionDTO = new CrearNotificacionDTO()
        


        this.restClient.post()
            .uri("/api/notificaciones")
            .body(notificacionDTO)
            .retrieve()
            .toBodilessEntity();
        //notificacion a notificacion-service [EVENTO]


    } */

}