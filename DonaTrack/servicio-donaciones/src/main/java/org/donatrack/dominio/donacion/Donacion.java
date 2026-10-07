package org.donatrack.dominio.donacion;

import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "donaciones_segmentadas")
public class Donacion {

    

    private String descripcion;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estado_actual_id")
    private EstadoDonacion estadoDonacion;
    
    @ElementCollection
    @CollectionTable(name = "donacion_items", joinColumns = @JoinColumn(name = "donacion_id"))
    private List<ItemBien> itemBienes;
    private LocalDateTime fechaIngreso;
    private String fotoEntrega;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "donacion_historial_estados",
            joinColumns = @JoinColumn(name = "donacion_id"),
            inverseJoinColumns = @JoinColumn(name = "estado_id"))
    private List<EstadoDonacion> historialEstadoDonacion = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "donante_id")
    private Donante donante;

    @ManyToOne
    @JoinColumn(name = "subcategoria_id")
    private Subcategoria subcategoria;

    @ManyToOne
    @JoinColumn(name = "entidad_beneficiaria_id")
    private EntidadBeneficiaria entidadAEntregar;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected Donacion() {
        this.itemBienes = new ArrayList<>();
    }

    public Donacion(Donante donante, List<ItemBien> itemBienes) {
        this.donante = donante;
        this.itemBienes = itemBienes;
    }

    public Donacion(Donante donante, ItemBien itemBien, Subcategoria subcategoria) {
        this.donante = donante;
        this.itemBienes = new ArrayList<>();
        this.itemBienes.add(itemBien);
        this.subcategoria = subcategoria;
        estadoDonacion = new EstadoEnDeposito();
        fechaIngreso = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntidadBeneficiaria getEntidadAEntregar() {
        return entidadAEntregar;
    }

    public void setEntidadAEntregar(EntidadBeneficiaria entidadAEntregar) {
        this.entidadAEntregar = entidadAEntregar;
    }

    public List<ItemBien> getItemBienes() {
        return itemBienes;
    }

    public void agregarItemBien(ItemBien itemBien) {
        itemBienes.add(itemBien);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoDonacion getEstadoDonacion() {
        return estadoDonacion;
    }

    public void setEstadoDonacion(EstadoDonacion estadoDonacion) {
        this.estadoDonacion = estadoDonacion;
        agregarListaEstadoDonacion(estadoDonacion);
    }

    public Subcategoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Subcategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    public int getCantidadTotal() {
        int total = 0;
        for (ItemBien item : itemBienes) {
            total += item.getCantidad();
        }
        return total;
    }

    public Donante getDonante() {
        return donante;
    }

    public void setDonante(Donante donante) {
        this.donante = donante;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public String getFotoEntrega() {
        return fotoEntrega;
    }

    public void setFotoEntrega(String foto) {
        fotoEntrega = foto;
    }

    public void siguiente(){
        estadoDonacion.siguiente(this);
    }
    public void siguiente(EntidadBeneficiaria entidad){
        estadoDonacion.siguiente(this, entidad);
    }

    public void falloEnEstado(){
        estadoDonacion.falloEnEstado(this);
    }

    public void falloEnEstado(String justificacion){
        estadoDonacion.falloEnEstado(this, justificacion);
    }

    public Boolean esEstadoValido(TipoEstado nuevoEstado){
        return this.estadoDonacion.esEstadoValido(nuevoEstado);
    }


    public void agregarListaEstadoDonacion(EstadoDonacion estadoObtenido) {
        this.historialEstadoDonacion.add(estadoObtenido);
    }

    public List<EstadoDonacion> getHistorialEstados() {
        return historialEstadoDonacion;
    }

    public List<Donacion> segmentarDonacion() {
        List<Donacion> donacionesSegmentadas = new ArrayList<>();
        List<ItemBien> bienesDonacion = this.itemBienes;

        for (ItemBien itemBien : bienesDonacion) {
            Subcategoria subcategoriaBien = itemBien.getBien().getSubcategoria();
            Donacion donacionDeSubcategoria = this.donacionDeSubcategoria(donacionesSegmentadas, subcategoriaBien);

            if (donacionesSegmentadas.isEmpty() || donacionDeSubcategoria == null) {
                Donacion donacionNueva = new Donacion(donante, itemBien, subcategoriaBien);
                donacionesSegmentadas.add(donacionNueva);
            } else {
                donacionDeSubcategoria.agregarItemBien(itemBien);
            }
        }

        return donacionesSegmentadas;
    }

    private Donacion donacionDeSubcategoria(List<Donacion> donaciones, Subcategoria subcategoria) {
        return donaciones.stream()
                .filter(d -> d.getItemBienes().stream().anyMatch(b -> b.getBien().getSubcategoria() == subcategoria))
                .findFirst()
                .orElse(null);
    }
}
