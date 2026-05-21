abstract class Necesidad {
    
    private EntidadBeneficiaria entidad;
    private Subcategoria subcategoria;
    private String descripcion;
    private Integer cantidad;
    private Boolean activa;

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
