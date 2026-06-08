import java.util.ArrayList;
import java.util.List;

public class RepositorioNecesidades {

    private static RepositorioNecesidades instance = null;
    private List<Necesidad> necesidades;

    public RepositorioNecesidades() {
        this.necesidades = new ArrayList<>();
    }

    public getInstance() {
        if (instance == null) {
            instance = new RepositorioNecesidades();
        }
        return instance;
    }

    public void agregarNecesidad(Necesidad necesidad) {
        necesidades.add(necesidad);
    }

    public void eliminarNecesidad(Necesidad necesidad) {
        necesidades.remove(necesidad);
    }
}