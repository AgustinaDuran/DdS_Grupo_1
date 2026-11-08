package org.donatrack.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.donatrack.controller.dto.PaginaDTO;
import org.donatrack.controller.dto.Asignaciones.DonacionPendienteDTO;
import org.donatrack.controller.dto.Asignaciones.RankingDonacionDTO;
import org.donatrack.controller.dto.Donaciones.DonacionDTO;
import org.donatrack.dominio.algoritmo.CoordinadorAsignacion;
import org.donatrack.dominio.algoritmo.ResultadoAsignacion;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.TipoEstado;
import org.donatrack.repository.DonacionesRepository;
import org.donatrack.repository.EntidadesBeneficiariasRepository;
import org.donatrack.repository.ResultadoAsignacionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Casos de uso de lectura del flujo de asignación. Arma los DTOs dentro de la transacción
// porque combinan dos agregados (la donación y su ranking) y recorren relaciones lazy.
@Service
public class AsignacionesService {

    private static final int TAMANIO_MAXIMO = 50;

    private final CoordinadorAsignacion coordinadorAsignacion;
    private final DonacionesRepository donacionesRepository;
    private final ResultadoAsignacionRepository resultadosRepository;
    private final EntidadesBeneficiariasRepository entidadesRepository;

    public AsignacionesService(CoordinadorAsignacion coordinadorAsignacion,
                               DonacionesRepository donacionesRepository,
                               ResultadoAsignacionRepository resultadosRepository,
                               EntidadesBeneficiariasRepository entidadesRepository) {
        this.coordinadorAsignacion = coordinadorAsignacion;
        this.donacionesRepository = donacionesRepository;
        this.resultadosRepository = resultadosRepository;
        this.entidadesRepository = entidadesRepository;
    }

    // Ejecución a demanda del cálculo nocturno. Devuelve cuántas donaciones procesó.
    public int ejecutarCalculo() {
        return coordinadorAsignacion.generarRecomendaciones().size();
    }

    // Donaciones en depósito con el resumen de su ranking. Lo que entró primero va primero.
    @Transactional(readOnly = true)
    public PaginaDTO<DonacionPendienteDTO> obtenerPendientes(int pagina, int tamanio, Long subcategoriaId) {
        Pageable pageable = paginar(pagina, tamanio, Sort.by("fechaIngreso").ascending().and(Sort.by("id")));
        Page<Donacion> donaciones = donacionesRepository.buscarPorEstado(TipoEstado.EN_DEPOSITO, subcategoriaId, pageable);

        // Los rankings de toda la página en una sola consulta, en lugar de una por donación.
        Map<Long, ResultadoAsignacion> resultadosPorDonacion = resultadosRepository
                .findByDonacionIdIn(donaciones.getContent().stream().map(Donacion::getId).toList())
                .stream()
                .collect(Collectors.toMap(r -> r.getDonacion().getId(), Function.identity()));

        return PaginaDTO.de(donaciones,
                donacion -> DonacionPendienteDTO.de(donacion, resultadosPorDonacion.get(donacion.getId())));
    }

    // Ranking completo de una donación. Vacío si no existe o ya no está en depósito.
    @Transactional(readOnly = true)
    public Optional<RankingDonacionDTO> obtenerRanking(Long donacionId) {
        return donacionesRepository.findById(donacionId)
                .filter(donacion -> donacion.getEstadoDonacion() != null
                        && donacion.getEstadoDonacion().getEstado() == TipoEstado.EN_DEPOSITO)
                .map(donacion -> RankingDonacionDTO.de(donacion,
                        resultadosRepository.findByDonacionId(donacionId).orElse(null)));
    }


    @Transactional(readOnly = true)
    public Optional<PaginaDTO<DonacionDTO>> obtenerDonacionesDeEntidad(Long entidadId, List<TipoEstado> estados,
                                                                     int pagina, int tamanio) {
        if (!entidadesRepository.existsById(entidadId)) {
            return Optional.empty();
        }

        Pageable pageable = paginar(pagina, tamanio,
                Sort.by("estadoDonacion.fechaIngresoEstado").descending().and(Sort.by("id").descending()));

        Page<Donacion> donaciones = (estados == null || estados.isEmpty())
                ? donacionesRepository.findByEntidadAEntregarId(entidadId, pageable)
                : donacionesRepository.findByEntidadAEntregarIdAndEstadoDonacionEstadoIn(entidadId, estados, pageable);

        return Optional.of(PaginaDTO.de(donaciones, DonacionDTO::new));
    }

    // Normaliza los parámetros: página desde 0 y tamaño entre 1 y TAMANIO_MAXIMO.
    private Pageable paginar(int pagina, int tamanio, Sort orden) {
        int tamanioValido = Math.min(Math.max(tamanio, 1), TAMANIO_MAXIMO);
        return PageRequest.of(Math.max(pagina, 0), tamanioValido, orden);
    }
}
