import java.util.ArrayList;
import java.util.List;

public class Categoria implements ComponenteCategoria {
    private String nombre;
    private List<ComponenteCategoria> componentes;

    public Categoria(String nombre){
        this.nombre= nombre;
        this.componentes = new ArrayList<>();
    }

    @Override
    public String getNombre(){
        return this.nombre;
    }

    @Override
    public void agregarComponente(ComponenteCategoria componenteAPoner){
        this.componentes.add(componenteAPoner);
    }

    @Override
    public void quitarComponente(ComponenteCategoria componenteAQuitar){
        this.componentes.remove(componenteAQuitar);
    }


} 
