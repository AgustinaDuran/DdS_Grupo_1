package org.donatrack.dominio.necesidades;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.categoria.Subcategoria;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "necesidades_recurrentes")
public class NecesidadRecurrente extends Necesidad{

    @Embedded
    private Periodo periodoTiempo;

    protected NecesidadRecurrente() {
    }

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

    public void setPeriodoTiempo(Periodo periodoTiempo){
        this.periodoTiempo = periodoTiempo;
    }
}
