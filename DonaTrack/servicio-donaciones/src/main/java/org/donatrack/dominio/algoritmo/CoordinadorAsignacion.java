package org.donatrack.dominio.algoritmo;

import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.Deposito;
import org.donatrack.dominio.donacion.EstadoEnDeposito;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.repository.EntidadesBeneficiariasRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class CoordinadorAsignacion {

    private List<AlgoritmoAsignacion> algoritmos;

    public CoordinadorAsignacion(List<AlgoritmoAsignacion> algoritmos) {
        this.algoritmos = algoritmos;
    }

    public List<ResultadoAsignacion> generarRecomendaciones() {
        List<EntidadBeneficiaria> entidades = EntidadesBeneficiariasRepository.getInstance().findAll();
        List<Donacion> deposito = Deposito.getInstance().getDonacionesEnDeposito();

        List<ResultadoAsignacion> resultados = new ArrayList<>();

        for (Donacion donacion : deposito) {
            resultados.add(generarRecomendacion(donacion, entidades));
        }

        return resultados;
    }

     private ResultadoAsignacion generarRecomendacion(Donacion donacion, List<EntidadBeneficiaria> entidades) {
        ResultadoAsignacion resultadoAsignacion = new ResultadoAsignacion(donacion, null, null);
        List<List<EntidadBeneficiaria>> rankings = new ArrayList<>();
        for (AlgoritmoAsignacion algoritmo : algoritmos) {
            rankings.add(algoritmo.generarRanking(donacion, entidades));
        }

        
        List<EntidadBeneficiaria> coincidencias = interseccion(rankings);

        
        if (!coincidencias.isEmpty()) {
            resultadoAsignacion.setCoincidencias(donacion, coincidencias);
            return resultadoAsignacion;
        } else {
            resultadoAsignacion.setSinCoincidencias(donacion, rankings);
            return resultadoAsignacion;
        }
    }
    
    public List<EntidadBeneficiaria> interseccion(List<List<EntidadBeneficiaria>> rankings) {

        if(rankings.isEmpty()) {
            return new ArrayList<>();
        }

        Set<EntidadBeneficiaria> enTodas = new HashSet<>(rankings.get(0));
        for (List<EntidadBeneficiaria> ranking : rankings) {
            enTodas.retainAll(ranking);
        }
        
        return new ArrayList<>(enTodas);
    }

}