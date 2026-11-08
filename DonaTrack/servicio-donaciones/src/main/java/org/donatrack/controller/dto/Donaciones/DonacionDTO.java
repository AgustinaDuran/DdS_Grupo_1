package org.donatrack.controller.dto.Donaciones;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.EstadoDonacion;
import org.donatrack.dominio.donacion.TipoEstado;
import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.controller.dto.Bienes.ResponseBienDTO;
import org.donatrack.controller.dto.Nombres;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

public class DonacionDTO {
    private Long id;
    private Long donanteId;
    private String donanteNombre;
    private Long entidadId;
    private String entidadNombre;
    private int cantidadTotal;
    private String descripcion;
    private Long subcategoriaId;
    private String subcategoriaNombre;
    private TipoEstado estado;
    private List<ResponseBienDTO> items = new ArrayList<>();
    private LocalDateTime fechaIngreso;
    private String fotoEntrega;

    public DonacionDTO(Donacion donacion) {
        this.id = donacion.getId();
        if (donacion.getDonante() != null) {
            this.donanteId = donacion.getDonante().getId();
            this.donanteNombre = Nombres.deDonante(donacion.getDonante());
        }
        // Entidad a la que se asignó la donación (null mientras está en depósito).
        EntidadBeneficiaria entidad = donacion.getEntidadAEntregar();
        if (entidad != null) {
            this.entidadId = entidad.getId();
            this.entidadNombre = Nombres.deEntidad(entidad);
        }
        if (donacion.getItemBienes() != null) {
            this.cantidadTotal = donacion.getCantidadTotal();
        }
        this.descripcion = donacion.getDescripcion();

        if (donacion.getSubcategoria() != null) {
            this.subcategoriaId = donacion.getSubcategoria().getId();
            this.subcategoriaNombre = donacion.getSubcategoria().getNombre();
        }

        EstadoDonacion estadoDonacion = donacion.getEstadoDonacion();
        if (estadoDonacion != null) {
            this.estado = estadoDonacion.getEstado();
        }

        if (donacion.getItemBienes() != null) {
            for (ItemBien item : donacion.getItemBienes()) {
                if (item.getBien() != null) {
                    this.items.add(new ResponseBienDTO(item.getBien()));
                }
            }
        }

        this.fechaIngreso = donacion.getFechaIngreso();
        this.fotoEntrega = donacion.getFotoEntrega();
    }

    public Long getId() {
        return id;
    }

    public Long getDonanteId() {
        return donanteId;
    }

    public String getDonanteNombre() {
        return donanteNombre;
    }

    public Long getEntidadId() {
        return entidadId;
    }

    public String getEntidadNombre() {
        return entidadNombre;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Long getSubcategoriaId() {
        return subcategoriaId;
    }

    public String getSubcategoriaNombre() {
        return subcategoriaNombre;
    }

    public TipoEstado getEstado() {
        return estado;
    }

    public List<ResponseBienDTO> getItems() {
        return items;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public String getFotoEntrega() {
        return fotoEntrega;
    }
}
