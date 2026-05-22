import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Donacion {
    private Donante donante;
    private String descripcion;
    private EstadoDonacion estadoDonacion;
    private Subcategoria subcategoria;
    private List<ItemBien> itemBienes;
    private LocalDateTime fechaIngreso;
    private String fotoEntrega;

    public Donacion(List<ItemBien> itemBienes){ //donacion instanciada por administrador
        this.itemBienes = itemBienes;
    }

    public Donacion(ItemBien itemBien){ //donacion instanciada por sistema, segmentada
        List<ItemBien> listaConElPrimerBien = new ArrayList<>();
        listaConElPrimerBien.add(itemBien);
        this.itemBienes = listaConElPrimerBien;
        estadoDonacion = EstadoDonacion.DISPONIBLE;
        fechaIngreso = LocalDateTime.now();
    }


    public List<ItemBien> getItemBienes(){
        return itemBienes;
    }

    public void agregarItemBien(ItemBien itemBien){
        itemBienes.add(itemBien);
    }

    public String getDescripcion(){
        return descripcion;
    }

    public EstadoDonacion getEstadoDonacion(){
        return estadoDonacion;
    }

    public Subcategoria getSubcategoria(){
        return subcategoria;
    }

    public void setSubcategoria(Subcategoria subcategoria){
        this.subcategoria = subcategoria;
    }


    public int getCantidadTotal(){
        int total = 0;
        for (ItemBien item : itemBienes) {
            total += item.getCantidad();
        }
        return total;
    }

    public Donante getDonante(){
        return donante;
    }

    public LocalDateTime getFechaIngreso(){
        return fechaIngreso;
    }

    public String getFotoEntrega(){
        return fotoEntrega;
    }

    public void setFotoEntrega(String foto){
        fotoEntrega = foto;
    }
    

}
