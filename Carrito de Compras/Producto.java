import java.util.List;
import java.time.LocalDate;

public class Producto {
   private String EAN13;
   private String nombre;
   private List<PrecioProducto> preciosHistoricos;
   private PrecioProducto producto;

   public String getEAN() {
      return EAN13;
   }

   public String getNombre() {
      return nombre;
   }

   public float getPrecio(LocalDate fechaVigencia) throws Exception {
      
      for (PrecioProducto precio : preciosHistoricos) {

        if (precio.getCumpleVigencia(fechaVigencia)) {
            return precio.getPrecio();
        }
      }
      
      throw new Exception("Producto no tiene precio disponible");
   }
}
