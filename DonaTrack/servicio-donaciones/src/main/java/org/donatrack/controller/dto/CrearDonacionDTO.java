package org.donatrack.controller.dto;

import java.util.List;
import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.bien.*;
import org.donatrack.dominio.donante.*;
import java.time.LocalDateTime;
import org.donatrack.dominio.categoria.Subcategoria;

public class CrearDonacionDTO { // por ahora muchos datos, vemos si hay convienen otros dtos
    Donante donante;
    String descripcion;
    List<ItemBien> items;


    public CrearDonacionDTO(){ 
    }

}
