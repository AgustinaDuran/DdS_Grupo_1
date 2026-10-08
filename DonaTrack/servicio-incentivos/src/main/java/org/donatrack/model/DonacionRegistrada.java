package org.donatrack.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "donaciones_registradas")
public class DonacionRegistrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreUsuario;
    private String subcategoria;
    private String organizacion;
    private LocalDate fechaIngreso;

    @ElementCollection
    @CollectionTable(name = "donacion_registrada_bienes",
            joinColumns = @JoinColumn(name = "donacion_id"))
    @Column(name = "bien")
    private List<String> bienes = new ArrayList<>();

    protected DonacionRegistrada() { } // para JPA

    public DonacionRegistrada(String nombreUsuario, String subcategoria, List<String> bienes,
                              String organizacion, LocalDate fechaIngreso) {
        this.nombreUsuario = nombreUsuario;
        this.subcategoria = subcategoria;
        this.bienes = bienes == null ? new ArrayList<>() : new ArrayList<>(bienes);
        this.organizacion = organizacion;
        this.fechaIngreso = fechaIngreso;
    }

    public Long GetId() { return id; }
    public String GetNombreUsuario() { return nombreUsuario; }
    public String GetSubcategoria() { return subcategoria; }
    public List<String> GetBienes() { return bienes; }
    public String GetOrganizacion() { return organizacion; }
    public LocalDate GetFechaIngreso() { return fechaIngreso; }

    public Integer GetCantidadBienes() {
        return bienes == null ? 0 : bienes.size();
    }
}