package org.donatrack.controller.dto.Donaciones;

import java.util.List;
import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.bien.*;
import org.donatrack.dominio.donante.*;
import java.time.LocalDateTime;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.controller.dto.Bienes.ItemBienDTO;

public class CrearDonacionDTO { // por ahora muchos datos, vemos si hay convienen otros dtos
    Long donanteId;
    String descripcion;
    List<ItemBienDTO> items;


    public CrearDonacionDTO(){ 
    }
    
    public Long getDonanteId() {
        return donanteId;
    }
    public void setDonanteId(Long donanteId) {
        this.donanteId = donanteId;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public List<ItemBienDTO> getItems() {
        return items;
    }
    public void setItems(List<ItemBienDTO> items) {
        this.items = items;
    }

}
