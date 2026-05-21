public class NecesidadRecurrente extends Necesidad{
    
    private Periodo periodoTiempo;

    public NecesidadRecurrente(EntidadBeneficiaria entidad, Subcategoria subcategoria, String descripcion, Integer cantidad, Boolean activa, Periodo periodo){
        this.entidad = entidad;
        this.subcategoria = subcategoria;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.activa = activa;
        this.periodoTiempo = periodo;
    }

    public Periodo getPeriodoTiempo(){
        return periodoTiempo;
    }
}
