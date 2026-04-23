import java.util.List;
import java.time.LocalDate;

public class Carrito {

    private List<Item> items;
    private LocalDate fechaCompra;

    private Cliente cliente;
    private Direccion direccionEnvio;
    private Direccion direccionCobro;
    private List<Pago> pagos;
    private EstadoCarrito estado;

    public void setEstado(EstadoCarrito estadoNuevo) {

        estado = estadoNuevo;
    }

    public void cerrar() {

        this.setEstado(EstadoCarrito.CERRADO);
    }

    public float getMontoPagado() {

        float sumaPagados = 0; // Monto pagado

        for (Pago pago : pagos) {

            sumaPagados += pago.getMonto();
        }

        return sumaPagados;
    }

    public float getMontoCarrito() {

        float montoAPagar = 0;

        if(cliente.esPreferencial()) {

            for (Item item : items) {

                montoAPagar += item.getDescuento();
            }
        }
        else {

            for (Item item : items) {

                montoAPagar += item.getPrecio();
            }
        }
       
        return montoAPagar;
    }

    public float getMontoDeuda() {

        return this.getMontoCarrito()-this.getMontoPagado(); // Monto que falta pagar
    }

    // Getters

    public LocalDate fechaCompra() {

        return fechaCompra;
    }
    
    public Direccion getDireccionEnvio() {

        return direccionEnvio;
    }

    public Direccion getDireccionCobro() {

        return direccionCobro;
    }

    public EstadoCarrito getEstadoCarrito() {

        return estado;
    }
}