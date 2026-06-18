package org.donatrack.dominio.donacion;

import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Donacion {

    private Donante donante;
    private String descripcion;
    private EstadoDonacion estadoDonacion;
    private Subcategoria subcategoria;
    private List<ItemBien> itemBienes;
    private LocalDateTime fechaIngreso;
    private String fotoEntrega;
    private List<EstadoDonacion> historialEstadoDonacion = new ArrayList<>();
    private EntidadBeneficiaria entidadAEntregar;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Donacion(List<ItemBien> itemBienes) { // donacion instanciada por administrador
        this.itemBienes = itemBienes;
    }

    public Donacion(ItemBien itemBien) { // donacion instanciada por sistema, segmentada
        List<ItemBien> listaConElPrimerBien = new ArrayList<>();
        listaConElPrimerBien.add(itemBien);
        this.itemBienes = listaConElPrimerBien;
        estadoDonacion = new EstadoEnDeposito();
        fechaIngreso = LocalDateTime.now();
        this.entidadAEntregar = null;
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

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public String getFotoEntrega() {
        return fotoEntrega;
    }

    public void setFotoEntrega(String foto) {
        fotoEntrega = foto;
    }

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

    public void agregarListaEstadoDonacion(EstadoDonacion estadoObtenido) {
        this.historialEstadoDonacion.add(estadoObtenido);
    }

    public List<EstadoDonacion> getHistorialEstados() {
        return historialEstadoDonacion;
    }
}
