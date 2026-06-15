package org.donatrack.service;

import org.donatrack.controller.dto.*;
import org.donatrack.model.*;
import org.donatrack.repository.DonanteRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnaliticaService {

    private final DonanteRepository donanteRepository;

    public AnaliticaService(DonanteRepository donanteRepository) {
        this.donanteRepository = donanteRepository;
    }

    public PerfilAnaliticoDTO obtenerEstadisticasGenerales(String nombreUsuario) {
        DonanteIncentivos donante = donanteRepository.findById(nombreUsuario).orElseThrow(() -> new RuntimeException("Donante no encontrado: " + nombreUsuario));

        int posicionRanking = calcularPosicionRanking(nombreUsuario);

        return new PerfilAnaliticoDTO(
                donante.getNombreUsuario(),
                donante.categoriaActual.getNombre(),
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
        
        for (Mision mision : donante.categoriaActual.getMisionesDelNivel()) {
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

    private int calcularPosicionRanking(String nombreUsuario) {
        Iterable<DonanteIncentivos> todos = donanteRepository.findAll();
        List<DonanteIncentivos> lista = new ArrayList<>();
        todos.forEach(lista::add);

        lista.sort((d1, d2) -> Integer.compare(d2.calcularTotalDonaciones(), d1.calcularTotalDonaciones())); //ordenar + a -

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNombreUsuario().equals(nombreUsuario)) {
                return i + 1;
            }
        }
        return 0;
    }
}