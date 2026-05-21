import java.util.List;

public class GestorNecesidades {
    
    private List<Necesidad> necesidades;

    public void agregarNecesidad(Necesidad necesidad){
        necesidades.add(necesidad);
    }

    public Donacion seleccionarDonacionSegun(Necesidad necesidad){

        for (Donacion donacion : GestorDonaciones.getDonaciones()){
            if (donacion.getsubcategoria().equals(necesidad.getSubcategoria())) {
                if (donacion.getCantidad() >= necesidad.getCantidad()) {
                    return donacion;
                }
            }

        }
        return null;
    }

}
