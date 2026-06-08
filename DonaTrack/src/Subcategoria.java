public class Subcategoria{
    private String nombre;
    private Bien tipoBien;

    public Subcategoria(String nombre,Bien tipoBien){
        this.nombre= nombre;
        this.tipoBien= tipoBien;
    }

    public Bien getBien(){
        return this.tipoBien;
    }

    public String getNombre(){
        return this.nombre;
    }

}