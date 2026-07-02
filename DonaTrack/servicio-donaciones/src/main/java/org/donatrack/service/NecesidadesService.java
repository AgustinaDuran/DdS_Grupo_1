package org.donatrack.service;

import java.util.List;

import org.donatrack.controller.dto.Necesidad.ActualizarNecesidadDTO;
import org.donatrack.controller.dto.Necesidad.CrearNecesidadDTO;
import org.donatrack.controller.dto.Necesidad.FiltrosNecesidadDTO;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.necesidades.Necesidad;
import org.donatrack.dominio.necesidades.NecesidadExtraordinaria;
import org.donatrack.dominio.necesidades.NecesidadRecurrente;
import org.donatrack.dominio.necesidades.Periodo;
import org.donatrack.dominio.necesidades.TipoNecesidad;
import org.donatrack.repository.NecesidadesRepository;
import org.springframework.stereotype.Service;

@Service
public class NecesidadesService {

    private final NecesidadesRepository necesidadesRepository;
    private final EntidadesBeneficiariasService entidadesBeneficiariasService;
    private final CategoriasService categoriasService;

    public NecesidadesService(NecesidadesRepository necesidadesRepository,
                              EntidadesBeneficiariasService entidadesBeneficiariasService,
                              CategoriasService categoriasService) {
        this.necesidadesRepository = necesidadesRepository;
        this.entidadesBeneficiariasService = entidadesBeneficiariasService;
        this.categoriasService = categoriasService;
    }

    public List<Necesidad> obtenerNecesidades(FiltrosNecesidadDTO filtrosNecesidadDTO) {
        return necesidadesRepository.buscarConFiltros(
                filtrosNecesidadDTO.getEntidadId(),
                filtrosNecesidadDTO.getSubcategoriaId(),
                filtrosNecesidadDTO.getTipo(),
                filtrosNecesidadDTO.getActiva());
    }

    public Necesidad obtenerNecesidadPorId(Long id) {
        return necesidadesRepository.findById(id);
    }

    public void registrarNecesidad(CrearNecesidadDTO nuevaNecesidad) {
        EntidadBeneficiaria entidad = null;
        if (nuevaNecesidad.getEntidadId() != null) {
            entidad = entidadesBeneficiariasService.obtenerEntidadPorId(nuevaNecesidad.getEntidadId());
        }
        Subcategoria subcategoria = null;
        if (nuevaNecesidad.getSubcategoriaId() != null) {
            subcategoria = categoriasService.obtenerSubcategoriaPorId(nuevaNecesidad.getSubcategoriaId());
        }

        Necesidad necesidad;
        if (nuevaNecesidad.getTipo() == TipoNecesidad.RECURRENTE) {
            Periodo periodo = new Periodo(nuevaNecesidad.getTipoPeriodo(), nuevaNecesidad.getFrecuencia());
            necesidad = new NecesidadRecurrente(entidad, subcategoria, nuevaNecesidad.getDescripcion(),
                    nuevaNecesidad.getCantidad(), true, periodo);
        } else {
            necesidad = new NecesidadExtraordinaria(entidad, subcategoria, nuevaNecesidad.getDescripcion(),
                    nuevaNecesidad.getCantidad(), true, nuevaNecesidad.getMotivo());
        }

        necesidadesRepository.save(necesidad);
        if (entidad != null) {
            entidad.registrarNecesidad(necesidad);
        }
    }

    public void actualizarNecesidad(Long id, ActualizarNecesidadDTO datosActualizacion) {
        Necesidad necesidad = necesidadesRepository.findById(id);
        if (necesidad == null) {
            throw new IllegalArgumentException("No se encontró la necesidad con el ID proporcionado");
        }
        if (datosActualizacion.getDescripcion() != null) {
            necesidad.setDescripcion(datosActualizacion.getDescripcion());
        }
        if (datosActualizacion.getCantidad() != null) {
            necesidad.setCantidad(datosActualizacion.getCantidad());
        }
        if (datosActualizacion.getActiva() != null) {
            necesidad.setActiva(datosActualizacion.getActiva());
        }
        if (necesidad instanceof NecesidadRecurrente recurrente && datosActualizacion.getTipoPeriodo() != null) {
            recurrente.setPeriodoTiempo(new Periodo(datosActualizacion.getTipoPeriodo(), datosActualizacion.getFrecuencia()));
        }
        if (necesidad instanceof NecesidadExtraordinaria extraordinaria && datosActualizacion.getMotivo() != null) {
            extraordinaria.setMotivo(datosActualizacion.getMotivo());
        }

        necesidadesRepository.save(necesidad);
    }

    public void eliminarNecesidadPorId(Long id) {
        necesidadesRepository.delete(id);
    }
}
