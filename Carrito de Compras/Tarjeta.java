public class Tarjeta{

    private String nombre;
    private MarcaTarjeta marcaTarjeta;
    private String ultimos4digitos;
    private float saldo;
    /* importante: planteamos saldo como forma de validación de tarjeta,
    si es necesario a futuro corresponde una clase banco o similar */
    public float getSaldo(){
        return saldo;
    }
}

