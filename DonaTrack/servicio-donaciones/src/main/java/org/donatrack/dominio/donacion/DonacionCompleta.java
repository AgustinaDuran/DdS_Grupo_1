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
@Table(name = "donacionesCompletas")
public class DonacionCompleta {

    

    private String descripcion;   
    private List<ItemBien> itemBienes;
    private LocalDateTime fechaIngreso;


    @ManyToOne
    //@JoinColumn //no se como se usa esto todavia
    private Donante donante;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public DonacionCompleta(Donante donante, List<ItemBien> itemBienes, String descripcion) {
        this.donante = donante;
        this.itemBienes = itemBienes;
        this.descripcion = descripcion;
        fechaIngreso = LocalDateTime.now();
    }

    public long getId() {
        return id;
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
