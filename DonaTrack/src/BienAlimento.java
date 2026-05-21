public class BienAlimento extends Bien {
    
    protected TipoUnidad unidad;
    protected Float cantidadBien;

    public BienAlimento(String nombre,String descripcion,String foto,TipoUnidad unidad, Float cantidadBien){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.unidad = unidad;
        this.cantidadBien = cantidadBien;
    }
    
}
