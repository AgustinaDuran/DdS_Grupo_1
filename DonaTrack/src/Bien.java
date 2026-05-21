abstract class Bien {
    
    protected String nombre;
    protected String descripcion;
    protected String foto;
    protected Subcategoria subcategoria;

    public String getNombre(){
        return this.nombre;
    }

    public String getDescripcion(){
        return this.descripcion;
    }
    
    public String getFoto(){
        return this.foto;
    }

    public Subcategoria getSubcategoria() {
        return subcategoria;
    }

    public void agregarFoto(String foto) {
        this.foto = foto;
    }

}
