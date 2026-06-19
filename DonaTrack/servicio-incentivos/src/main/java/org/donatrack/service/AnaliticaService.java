package org.donatrack.service;

import org.donatrack.controller.dto.*;
import org.donatrack.model.*;
import org.donatrack.repository.DonanteRepository;
import org.donatrack.repository.RankingRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnaliticaService {

    private final DonanteRepository donanteRepository;
    private final RankingRepository rankingRepository;

    public AnaliticaService(DonanteRepository donanteRepository, RankingRepository rankingRepository) {
        this.donanteRepository = donanteRepository;
        this.rankingRepository = rankingRepository;
    }

    public PerfilAnaliticoDTO obtenerEstadisticasGenerales(String nombreUsuario) {
        DonanteIncentivos donante = donanteRepository.findById(nombreUsuario).orElseThrow(() -> new RuntimeException("Donante no encontrado: " + nombreUsuario));

        Integer posicionRanking = calcularPosicionRanking(nombreUsuario);

        return new PerfilAnaliticoDTO(
                donante.getNombreUsuario(),
                donante.getCategoriaActual().getNombre(),
                donante.getDonaciones(),
                donante.obtenerEvolucionDonacionesPorPeriodo(), //falta ver el tema del grafico y como se calcula
                donante.obtenerComparacionMensual(YearMonth.now(), YearMonth.now().minusMonths(1)), 
                donante.calcularOrganizacionesAyudadas(),
                donante.calcularImpactoAcumulado(),
                posicionRanking
        );
    }

    public List<MisionProgresoDTO> obtenerProgresoMisiones(String nombreUsuario) {
        DonanteIncentivos donante = donanteRepository.findById(nombreUsuario).orElseThrow(() -> new RuntimeException("Donante no encontrado: " + nombreUsuario));

        List<MisionProgresoDTO> progresoList = new ArrayList<>();
        
        for (Mision mision : donante.getCategoriaActual().getMisionesDelNivel()) {
            progresoList.add(new MisionProgresoDTO(
                    mision.getDescripcion(),
                    mision.getProgresoActual(donante),
                    mision.getObjetivoAsignado(),
                    mision.getDistanciaRestante(donante),
                    mision.getInsigniaOtorgada().getNombre()
            ));
        }
        return progresoList;
    }

    public List<InsigniaDTO> obtenerInsignias(String nombreUsuario) {
        DonanteIncentivos donante = donanteRepository.findById(nombreUsuario).orElseThrow(() -> new RuntimeException("Donante no encontrado: " + nombreUsuario));

        List<InsigniaDTO> dtos = new ArrayList<>();
        for (Insignia insignia : donante.getInsigniasGanadas()) {
            dtos.add(new InsigniaDTO(insignia.getNombre(),insignia.getImagen()));
        }
        return dtos;
    }

    private Integer calcularPosicionRanking(String nombreUsuario) {
        if (!donanteRepository.existsById(nombreUsuario)) {
            return 0;
        }
    
        long personasAdelante = donanteRepository.countDonantesConMasDonaciones(nombreUsuario);
        return (int) (personasAdelante + 1);
    }

    /**
     * Registra una donación recibida desde el Servicio de Donaciones e impacta
     * el progreso de incentivos del donante.
     */
    public void registrarDonacionDeUsuario(String nombreUsuario, Donacion donacion) {
        DonanteIncentivos donante = donanteRepository.findById(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Donante no encontrado: " + nombreUsuario));
        donante.registrarActividad(donacion);
        donanteRepository.save(donante);
    }

    public PodioMensualDTO obtenerPodioDestacadoDelMes(YearMonth mesAConsultar) {
        RankingMensual ranking = rankingRepository.findById(mesAConsultar.toString()).orElseThrow(() -> new RuntimeException("Aún no se ha procesado el ranking para: " + mesAConsultar));

        List<PodioMensualDTO.PuestoGanador> destacadosDTO = new ArrayList<>();
        for (PuestoRanking puesto : ranking.getPodio()) {
            destacadosDTO.add(new PodioMensualDTO.PuestoGanador(puesto.getPosicion(), puesto.getNombreUsuario(), puesto.getMisionesCumplidasEnElMes()));
        }

    return new PodioMensualDTO(ranking.getIdPeriodo(), destacadosDTO);
    }
}