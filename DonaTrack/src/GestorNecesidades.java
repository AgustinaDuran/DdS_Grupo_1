import java.util.List;

public class GestorNecesidades {
    
    private List<Necesidad> necesidades;

    public void agregarNecesidad(Necesidad necesidad){
        necesidades.add(necesidad);
    }

    public Donacion seleccionarDonacionSegun(Necesidad necesidad){
        
        List<Donacion> donaciones = GestorDonaciones.getDonaciones();

        for (Donacion donacion : donaciones){
            if (donacion.getSubcategoria().equals(necesidad.getSubcategoria())) {
                if (donacion.getCantidadTotal() >= necesidad.getCantidad()) {
                    return donacion;
                }
            }

        }
        return null;
    }

}
