import java.util.ArrayList;
import java.util.List;

public class Deposito { // singleton

    private static Deposito instance = null;
    private List<Donacion> donaciones; // a reemplazar por un


    private Deposito() { //constructor privado
        donaciones = new ArrayList<>();
    }

    public static Deposito getInstance() {
        if (instance == null)
            instance = new Deposito();

        return instance;
    }

    public void agregarDonaciones(List<Donacion> donaciones){
        this.donaciones.addAll(donaciones);
    }

    public List<Donacion> getDonaciones(){
        return donaciones;
    }

}
