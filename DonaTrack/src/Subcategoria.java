public class Subcategoria implements ComponenteCategoria{
    private String nombre;
    private Bien tipoBien;

    public Subcategoria(String nombre,Bien tipoBien){
        this.nombre= nombre;
        this.tipoBien= tipoBien;
    }

    public Bien getBien(){
        return this.tipoBien;
    }

    @Override
    public String getNombre(){
        return this.nombre;
    }

    @Override
    public void agregarComponente(ComponenteCategoria componenteAPoner){
        throw new UnsupportedOperationException("No se pueden agregar componentes a una Subcategoría (Es una hoja)");
    }

    @Override
    public void quitarComponente(ComponenteCategoria componenteAQuitar){
       throw new UnsupportedOperationException("Una Subcategoria no tiene componentes(Es una hoja)");
    }


}