public class Item{
    
    private Carrito carrito;
    private Producto producto;
    private Integer cantidad;
    private Float precioUnitario;


    public float getPrecio() {
        return cantidad * precioUnitario;
    }

    public float getPrecioOficial() {
        
        try {
            
            return producto.getPrecio(carrito.fechaCompra());
            
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

    public float getDescuento() {
        return getPrecio() - getPrecioOficial();
    }
}
