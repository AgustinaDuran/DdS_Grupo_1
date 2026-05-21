abstract class Necesidad {
    
    protected EntidadBeneficiaria entidad;
    protected Subcategoria subcategoria;
    protected String descripcion;
    protected Integer cantidad;
    protected Boolean activa;


    public Integer getCantidad(){
        return cantidad;
    }

    public Subcategoria getSubcategoria(){
        return subcategoria;
    }

    public void marcarComoSaldada(){
        this.activa = false;
    }

}
