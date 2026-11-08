package org.donatrack.dominio.algoritmo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;


@Entity
@Table(name = "resultados_asignacion")
public class ResultadoAsignacion {

    public static final String LISTA_COINCIDENCIAS = "COINCIDENCIAS";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "donacion_id", nullable = false, unique = true)
    private Donacion donacion;

    @Column(name = "tiene_coincidencias", nullable = false)
    private boolean tieneCoincidencias;

    @Column(name = "fecha_calculo", nullable = false)
    private LocalDateTime fechaCalculo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resultado_id", nullable = false)
    @OrderBy("lista ASC, posicion ASC")
    private List<ItemRankingAsignacion> items = new ArrayList<>();

    protected ResultadoAsignacion() {
    }

    // rankingsPorAlgoritmo: nombre del algoritmo -> su ranking, en orden.
    public ResultadoAsignacion(Donacion donacion,
                               Map<String, List<EntidadBeneficiaria>> rankingsPorAlgoritmo,
                               List<EntidadBeneficiaria> coincidencias) {
        this.donacion = donacion;
        recalcular(rankingsPorAlgoritmo, coincidencias);
    }

    // Reemplaza el ranking por uno nuevo, conservando la misma fila.
    public void recalcular(Map<String, List<EntidadBeneficiaria>> rankingsPorAlgoritmo,
                           List<EntidadBeneficiaria> coincidencias) {
        items.clear();

        if (rankingsPorAlgoritmo != null) {
            rankingsPorAlgoritmo.forEach(this::agregarLista);
        }
        if (coincidencias != null) {
            agregarLista(LISTA_COINCIDENCIAS, coincidencias);
        }

        this.tieneCoincidencias = coincidencias != null && !coincidencias.isEmpty();
        this.fechaCalculo = LocalDateTime.now();
    }

    private void agregarLista(String lista, List<EntidadBeneficiaria> entidades) {
        for (int i = 0; i < entidades.size(); i++) {
            items.add(new ItemRankingAsignacion(entidades.get(i), lista, i + 1));
        }
    }

    public boolean tieneCoincidencias() {
        return tieneCoincidencias;
    }

    public List<EntidadBeneficiaria> getCoincidencias() {
        return entidadesDeLista(LISTA_COINCIDENCIAS);
    }

    // Nombre del algoritmo -> su ranking, en orden. No incluye la lista de coincidencias.
    public Map<String, List<EntidadBeneficiaria>> getRankingsPorAlgoritmo() {
        Map<String, List<EntidadBeneficiaria>> rankings = new LinkedHashMap<>();
        items.stream()
                .filter(item -> !LISTA_COINCIDENCIAS.equals(item.getLista()))
                .sorted(Comparator.comparing(ItemRankingAsignacion::getLista)
                        .thenComparingInt(ItemRankingAsignacion::getPosicion))
                .forEach(item -> rankings
                        .computeIfAbsent(item.getLista(), k -> new ArrayList<>())
                        .add(item.getEntidad()));
        return rankings;
    }

    private List<EntidadBeneficiaria> entidadesDeLista(String lista) {
        return items.stream()
                .filter(item -> lista.equals(item.getLista()))
                .sorted(Comparator.comparingInt(ItemRankingAsignacion::getPosicion))
                .map(ItemRankingAsignacion::getEntidad)
                .toList();
    }

    public List<ItemRankingAsignacion> getItems() {
        return List.copyOf(items);
    }

    public Long getId() {
        return id;
    }

    public Donacion getDonacion() {
        return donacion;
    }

    public LocalDateTime getFechaCalculo() {
        return fechaCalculo;
    }

    // Si hay coincidencias, solo se puede elegir entre ellas; si no, entre cualquiera de las listas.
    public void confirmarDestinoFinal(EntidadBeneficiaria entidad) {
        List<EntidadBeneficiaria> elegibles = tieneCoincidencias
                ? getCoincidencias()
                : getRankingsPorAlgoritmo().values().stream().flatMap(List::stream).toList();

        if (!elegibles.contains(entidad)) {
            throw new IllegalArgumentException("La entidad no pertenece al ranking");
        }

        donacion.siguiente(entidad);
    }
}
