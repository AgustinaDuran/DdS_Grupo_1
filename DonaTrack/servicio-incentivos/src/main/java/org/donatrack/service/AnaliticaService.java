package org.donatrack.service;

import org.donatrack.controller.dto.*;
import org.donatrack.model.*;
import org.donatrack.repository.DonanteRepository;
import org.donatrack.repository.RankingRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class AnaliticaService {

    private final DonanteRepository donanteRepository;
    private final RankingRepository rankingRepository;
    private final AutomatizacionService automatizacionService;

    public AnaliticaService(DonanteRepository donanteRepository, RankingRepository rankingRepository, AutomatizacionService automatizacionService) {
        this.donanteRepository = donanteRepository;
        this.rankingRepository = rankingRepository;
        this.automatizacionService = automatizacionService;
    }

    public void RegistrarDonacionDeUsuario(String nombreUsuario, DonacionDTO donacionDto) {
        DonanteIncentivos donante = donanteRepository.findById(nombreUsuario).orElseThrow(() -> new RuntimeException("Donante no encontrado: " + nombreUsuario));

        List<Mision> misionesRecienCumplidas = donante.RegistrarActividad(donacionDto);

        donanteRepository.save(donante);

        for (Mision mision : misionesRecienCumplidas) {
            automatizacionService.NotificarInsigniaGanada(donante.GetNombreUsuario(), mision.GetDescripcion(), mision.GetInsigniaOtorgada().GetImagen());
        }
    }

    public PerfilAnaliticoDTO ObtenerEstadisticasGenerales(String nombreUsuario) {
        DonanteIncentivos donante = donanteRepository.findById(nombreUsuario).orElseThrow(() -> new RuntimeException("Donante no encontrado: " + nombreUsuario));

        Integer posicionRanking = CalcularPosicionRanking(nombreUsuario);

        return new PerfilAnaliticoDTO(
                donante.GetNombreUsuario(),
                donante.GetNombreCategoriaActual(),
                donante.GetDonaciones(),
                donante.ObtenerEvolucionDonacionesPorPeriodo(donante.GetMesPrimeraDonacion(), YearMonth.now()),
                donante.ObtenerComparacionMensual(YearMonth.now(), YearMonth.now().minusMonths(1)), 
                donante.CalcularOrganizacionesAyudadas(),
                donante.CalcularImpactoAcumulado(),
                posicionRanking
        );
    }

    public List<MisionProgresoDTO> ObtenerProgresoMisiones(String nombreUsuario) {
        DonanteIncentivos donante = donanteRepository.findById(nombreUsuario).orElseThrow(() -> new RuntimeException("Donante no encontrado: " + nombreUsuario));

        List<MisionProgresoDTO> progresoList = new ArrayList<>();
        
        for (Mision mision : donante.GetCategoriaActual().GetMisionesDelNivel()) {
            progresoList.add(new MisionProgresoDTO(
                    mision.GetDescripcion(),
                    mision.GetProgresoActual(donante),
                    mision.GetObjetivoAsignado(),
                    mision.GetDistanciaRestante(donante),
                    mision.GetInsigniaOtorgada().GetNombre()
            ));
        }
        return progresoList;
    }

    public List<InsigniaDTO> ObtenerInsignias(String nombreUsuario) {
        DonanteIncentivos donante = donanteRepository.findById(nombreUsuario).orElseThrow(() -> new RuntimeException("Donante no encontrado: " + nombreUsuario));

        List<InsigniaDTO> dtos = new ArrayList<>();
        for (Insignia insignia : donante.GetInsigniasGanadas()) {
            dtos.add(new InsigniaDTO(insignia.GetNombre(),insignia.GetImagen()));
        }
        return dtos;
    }

    private Integer CalcularPosicionRanking(String nombreUsuario) {
        if (!donanteRepository.existsById(nombreUsuario)) {
            return 0;
        }
    
        Long personasAdelante = donanteRepository.countDonantesConMasDonaciones(nombreUsuario);
        return (int) personasAdelante + 1;
    }

    public PodioMensualDTO ObtenerPodioDestacadoDelMes(YearMonth mesAConsultar) {
        RankingMensual ranking = rankingRepository.findById(mesAConsultar.toString()).orElseThrow(() -> new RuntimeException("Aún no se ha procesado el ranking para: " + mesAConsultar));

        List<PodioMensualDTO.PuestoGanador> destacadosDTO = new ArrayList<>();
        for (PuestoRanking puesto : ranking.GetPodio()) {
            destacadosDTO.add(new PodioMensualDTO.PuestoGanador(puesto.GetPosicion(), puesto.GetNombreUsuario(), puesto.GetMisionesCumplidasEnElMes()));
        }

        return new PodioMensualDTO(ranking.GetIdPeriodo(), destacadosDTO);
    }
}