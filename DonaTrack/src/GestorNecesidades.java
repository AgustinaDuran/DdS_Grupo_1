import java.util.List;

public class GestorNecesidades {

    public void agregarNecesidad(Necesidad necesidad){
        RepositorioNecesidades.necesidades.add(necesidad);
    }

    public void eliminarNecesidad(Necesidad necesidad){
        RepositorioNecesidades.necesidades.remove(necesidad);
    }

    public Donacion seleccionarDonacionSegun(Necesidad necesidad){
        
        List<Donacion> donaciones = Deposito.getDonaciones();

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
