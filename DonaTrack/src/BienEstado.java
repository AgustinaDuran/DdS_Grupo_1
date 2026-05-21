public class BienEstado extends Bien {
    
    private Boolean estadoUso;

    public BienEstado(String nombre,String descripcion,String foto, Boolean estadoUso){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.estadoUso = estadoUso;
    }

    public Boolean getEstadoUso(){
        return estadoUso;
    }

}
