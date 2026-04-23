public class Pago {
    
    private Carrito carrito;
    private Tarjeta tarjeta;
    private float monto;

    // Por el momento solo verifica que el saldo de la tarjeta sea mayor al monto a pagar

    public boolean verificarTarjeta() {

        return tarjeta.getSaldo() >= monto;
    }

    public Carrito getCarrito() {

        return carrito;
    }

    public float getMonto() {

        return monto;
    }
}

