import java.util.ArrayList;
import java.util.List;

public abstract class Donante {
    protected List<Contacto> contactos = new ArrayList<>();
    protected List<Donacion>  donacionesHistoricas = new ArrayList<>();



    void agregarContacto(Contacto nuevoContacto) {
        contactos.add(nuevoContacto);
    }

    public void filtrarPorEstado(EstadoDonacion estado){
        List<Donacion> donaciones = donacionesHistoricas.stream()
                .filter(d -> d.getEstadoDonacion() == estado)
                .toList();

        System.out.println("Donaciones filtradas por " + estado.name());

        for(Donacion donacion: donaciones) {
            System.out.println("Donacion: " + donacion);
        }
    }

    public void filtrarPorSubcategoria(Subcategoria subcategoria){
        List<Donacion> donaciones = donacionesHistoricas.stream()
                .filter(d -> d.getSubcategoria() == subcategoria)
                .toList();

        System.out.println("Donaciones filtradas por " + subcategoria.getNombre());

        for(Donacion donacion: donaciones) {
            System.out.println("Donacion: " + donacion);
        }
    }

    public void visualizarDonaciones() {

        System.out.println("Donaciones historicas");

        for(Donacion donacion: donacionesHistoricas) {
            System.out.println("Donacion: " + donacion);
        }
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    
}
