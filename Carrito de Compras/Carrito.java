import java.util.List;
import java.time.LocalDate;

public class Carrito{
    private List<Item> items;
    private LocalDate fechaCompra;

    private Cliente cliente;
    private Direccion direccionEnvio;
    private Direccion direccionCobro;
    private List<Pago> pagos;
    private EstadoCarrito estado;

    public LocalDate fechaCompra(){
        return fechaCompra;
    }
    public void setEstado(EstadoCarrito estadoNuevo){
        estado = estadoNuevo;
    }

    public void cerrar(){
        this.setEstado(EstadoCarrito.CERRADO);
    }

    public float getMontoPagado(){
        //monto que pago
        float sumaPagados = 0;
        for (Pago pago : pagos){
            sumaPagados += pago.getMonto();
        }
        return sumaPagados;
    }
    public float getMontoCarrito(){
        //monto a pagar

        float montoAPagar = 0;
        if(cliente.esPreferencial()){
            for (Item item : items){
                montoAPagar += item.getDescuento();
        }
        }
        else{
            for (Item item : items){
                montoAPagar += item.getPrecio();
            }
        }
       
        return montoAPagar;
    }
    public float getMontoDeuda(){
        //monto que falta pagar
        
        return this.getMontoCarrito()-this.getMontoPagado();
    }
}