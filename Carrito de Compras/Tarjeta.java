public class Tarjeta {

    private String nombre;
    private MarcaTarjeta marcaTarjeta;
    private String ultimos4digitos;
    private float saldo; // La variable saldo se plantea por el método verificarTarjeta() de la clase Pago

    public String getNombre() {

        return nombre;
    }

    public MarcaTarjeta getMarcaTarjeta() {

        return marcaTarjeta;
    }

    public String getUltimosDigitos() {

        return ultimos4digitos;
    }

    public float getSaldo() {

        return saldo;
    }
}

