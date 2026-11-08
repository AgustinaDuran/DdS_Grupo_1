package org.donatrack.dominio.algoritmo;

import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "items_ranking_asignacion")
public class ItemRankingAsignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "entidad_id", nullable = false)
    private EntidadBeneficiaria entidad;

    // Nombre del algoritmo 
    @Column(nullable = false, length = 50)
    private String lista;

    @Column(nullable = false)
    private int posicion;

    protected ItemRankingAsignacion() {
    }

    public ItemRankingAsignacion(EntidadBeneficiaria entidad, String lista, int posicion) {
        this.entidad = entidad;
        this.lista = lista;
        this.posicion = posicion;
    }

    public Long getId() {
        return id;
    }

    public EntidadBeneficiaria getEntidad() {
        return entidad;
    }

    public String getLista() {
        return lista;
    }

    public int getPosicion() {
        return posicion;
    }
}
