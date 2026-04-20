public class Pago {
    private Carrito carrito;
    private Tarjeta tarjeta;
    private float monto;

    // no lleva a nada por ahora, podria desvalidar el pago en la cuenta del carrito?
    public boolean verificarTarjeta() {
        return tarjeta.getSaldo() >= monto;
    }

    public float getMonto() {
        return monto;
    }
}

