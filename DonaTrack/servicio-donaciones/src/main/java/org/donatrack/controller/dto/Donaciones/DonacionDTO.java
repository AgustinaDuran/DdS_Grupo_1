package org.donatrack.controller.dto.Donaciones;

import java.util.List;
import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.bien.*;
import org.donatrack.dominio.donante.*;
import java.time.LocalDateTime;
import org.donatrack.dominio.categoria.Subcategoria;

public class DonacionDTO { // por ahora muchos datos, vemos si hay convienen otros dtos
    long id;
    long donante_id;
    String descripcion;
    Subcategoria subcategoria;
    EstadoDonacion estadoDonacion;
    List<ItemBien> items;
    LocalDateTime fechaIngreso;
    String fotoEntrega;

    public DonacionDTO(Donacion donacion) {
        this.id = donacion.getId();
        this.donante_id = donacion.getDonante().getId(); //devolver todo el donante o solo el id
        this.descripcion = donacion.getDescripcion();
        this.subcategoria = donacion.getSubcategoria();
        this.estadoDonacion = donacion.getEstadoDonacion();
        this.items = donacion.getItemBienes();
        this.fechaIngreso = donacion.getFechaIngreso();
        this.fotoEntrega = donacion.getFotoEntrega();
    }

}
