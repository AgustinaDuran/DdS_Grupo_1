package org.donatrack.dominio.algoritmo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.donacion.Donacion;

public class ResultadoAsignacion{
    private Donacion donacion;
    private List<EntidadBeneficiaria> rankingPorCoincidencias = new ArrayList<>();
    private List<List<EntidadBeneficiaria>> rankingsPorAlgoritmo = new ArrayList<>();

     public ResultadoAsignacion(Donacion donacion, List<List<EntidadBeneficiaria>> rankingsPorAlgoritmo, List<EntidadBeneficiaria> rankingPorCoincidencias) {
        this.donacion = donacion;
        this.rankingsPorAlgoritmo = rankingsPorAlgoritmo;
        this.rankingPorCoincidencias = rankingPorCoincidencias;
    }

    public boolean tieneCoincidencias() {
        return !rankingPorCoincidencias.isEmpty();
    }

    public List<EntidadBeneficiaria> getCoincidencias() {
        return rankingPorCoincidencias;
    }

    public List<List<EntidadBeneficiaria>> getRankingsPorAlgoritmo() {
        return rankingsPorAlgoritmo;
    }

    public Donacion getDonacion() {
        return donacion;
    }

    public void setSinCoincidencias(Donacion donacion, List<List<EntidadBeneficiaria>> rankings) {
        this.donacion = donacion;
        this.rankingsPorAlgoritmo = rankings;
    }

    public void setCoincidencias(Donacion donacion, List<EntidadBeneficiaria> coincidencias) {
        this.donacion = donacion;
        this.rankingPorCoincidencias = coincidencias;
    }

    
}