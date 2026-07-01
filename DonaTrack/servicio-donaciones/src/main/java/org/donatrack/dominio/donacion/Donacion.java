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
@Table(name = "donacionesSegmentadas")
public class Donacion {

    

    private String descripcion;
    private EstadoDonacion estadoDonacion;
    
    private List<ItemBien> itemBienes;
    private LocalDateTime fechaIngreso;
    private String fotoEntrega;
    private List<EstadoDonacion> historialEstadoDonacion = new ArrayList<>();


    @ManyToOne
    //@JoinColumn //no se como se usa esto todavia
    private Donante donante;

    @ManyToOne
    private Subcategoria subcategoria;

    
    private EntidadBeneficiaria entidadAEntregar;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Donacion(Donante donante, List<ItemBien> itemBienes) { // donacion instanciada por administrador
        this.donante = donante;
        this.itemBienes = itemBienes;
    }

    public Donacion(Donante donante, ItemBien itemBien, Subcategoria subcategoria) { // donacion instanciada por sistema, segmentada
        this.donante = donante;
        this.itemBienes = new ArrayList<>();
        this.itemBienes.add(itemBien);
        this.subcategoria = subcategoria;
        estadoDonacion = new EstadoEnDeposito();
        fechaIngreso = LocalDateTime.now();
    }


    public long getId() {
        return id;
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


    /*
    public void asignar(EntidadBeneficiaria e) {
        estadoDonacion.asignar(this);
        setEntidadAEntregar(e);

    }

    public void planificarRuta() {
        estadoDonacion.planificarRuta(this);
    }

    public void iniciarTraslado() {
        estadoDonacion.iniciarTraslado(this);
    }

    public void confirmarEntrega() {
        estadoDonacion.confirmarEntrega(this);
        entidadAEntregar.agregarDonacionRecibida(this);
    }

    public void registrarEntregaFallida(String justificacion) {
        estadoDonacion.registrarEntregaFallida(this, justificacion);
    }

    public void marcarComoVencida() {
        estadoDonacion.marcarComoVencida(this);
    }

    public void volverADeposito() {
        estadoDonacion.volverADeposito(this);
    }
*/
    public void agregarListaEstadoDonacion(EstadoDonacion estadoObtenido) {
        this.historialEstadoDonacion.add(estadoObtenido);
    }

    public List<EstadoDonacion> getHistorialEstados() {
        return historialEstadoDonacion;
    }

    public List<Donacion> segmentarDonacion() {
        Donante Donante = this.donante;
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
