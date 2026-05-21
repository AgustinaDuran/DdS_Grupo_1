import java.time.LocalDateTime;
import java.util.List;

public class Donacion {
    private Donante donante;
    private String descripcion;
    private EstadoDonacion estadoDonacion;
    private Subcategoria subcategoria;
    private List<ItemBien> itemBienes;
    private LocalDateTime fechaIngreso;

    public Donacion(List<ItemBien> itemBienes){
        this.itemBienes = itemBienes;
        estadoDonacion = EstadoDonacion.DISPONIBLE;
        fechaIngreso = LocalDateTime.now();
    }

    public void asignarSubcategoria(Subcategoria subcategoria){
        this.subcategoria = subcategoria;
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

}
