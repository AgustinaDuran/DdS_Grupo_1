public class NecesidadExtraordinaria extends Necesidad {
 
    private String motivo;

    public NecesidadExtraordinaria(EntidadBeneficiaria entidad, Subcategoria subcategoria, String descripcion, Integer cantidad, Boolean activa, String motivo){
        this.entidad = entidad;
        this.subcategoria = subcategoria;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.activa = activa;
        this.motivo = motivo;
    }

    

}
