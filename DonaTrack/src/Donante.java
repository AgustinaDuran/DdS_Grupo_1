import java.util.ArrayList;
import java.util.List;

public abstract class Donante {
    private List<Contacto> contactos = new ArrayList<>();
    private List<Donacion>  donacionesHistoricas = new ArrayList<>();

    public Donante() {
    }

    void agregarContacto(Contacto nuevoContacto) {
        contactos.add(nuevoContacto);
    }

    public List<Donacion> filtrarPorEstado(EstadoDonacion estado){
        List<Donacion> donaciones = donacionesHistoricas.stream()
                .filter(d -> d.getEstadoDonacion == estado)
                .toList();

        System.out.println("Donaciones filtradas por " + estado.name());

        for(Donacion donacion: donaciones) {
            System.out.println("Donacion: " + donacion);
        }
    }

    public List<Donacion> filtrarPorSubcategoria(Subcategoria subcategoria){
        List<Donacion> donaciones = donacionesHistoricas.stream()
                .filter(d -> d.getSubcategoria == subcategoria)
                .toList();

        System.out.println("Donaciones filtradas por " + subcategoria.getNombre());

        for(Donacion donacion: donaciones) {
            System.out.println("Donacion: " + donacion);
        }
    }

    public List<Donacion> visualizarDonaciones() {

        System.out.println("Donaciones historicas");

        for(Donacion donacion: donacionesHistoricas) {
            System.out.println("Donacion: " + donacion);
        }
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public Mail getMail() {
        return mail;
    }
}
