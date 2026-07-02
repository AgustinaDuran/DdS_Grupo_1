package org.donatrack.controller.dto.Donaciones;

import java.util.List;
import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.bien.*;
import org.donatrack.dominio.donante.*;
import java.time.LocalDateTime;
import org.donatrack.dominio.categoria.Subcategoria;

public class CrearDonacionDTO { // por ahora muchos datos, vemos si hay convienen otros dtos
    long donanteId;
    String descripcion; // por ahora no se usa
    List<ItemBien> items;


    public CrearDonacionDTO(){ 
    }
    
    public long getDonanteId() {
        return donanteId;
    }
    public void setDonanteId(long donanteId) {
        this.donanteId = donanteId;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public List<ItemBien> getItems() {
        return items;
    }
    public void setItems(List<ItemBien> items) {
        this.items = items;
    }

}
