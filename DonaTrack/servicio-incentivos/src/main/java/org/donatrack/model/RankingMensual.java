package org.donatrack.model;

import jakarta.persistence.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rankings_mensuales")
public class RankingMensual {

    @Id
    private String idPeriodo;
    @OneToMany(cascade = CascadeType.ALL) //muchos puestos -> guarda los 3 primeros juntos
    @JoinColumn(name = "ranking_periodo_id")

    private List<PuestoRanking> podio = new ArrayList<>();

    public RankingMensual() {} //pa leer la bd

    public RankingMensual(YearMonth periodo) {
        this.idPeriodo = periodo.toString();
    }

    public void agregarAlPodio(PuestoRanking puesto) {
        this.podio.add(puesto);
    }

    public String getIdPeriodo() { return idPeriodo; }
    public List<PuestoRanking> getPodio() { return podio; }
}