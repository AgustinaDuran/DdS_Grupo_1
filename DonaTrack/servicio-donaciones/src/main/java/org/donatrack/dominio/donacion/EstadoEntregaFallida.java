package org.donatrack.dominio.donacion;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

public class EstadoEntregaFallida extends EstadoDonacion {

    private String justificacion;
    private EntidadBeneficiaria entidadBeneficiaria;

    public EstadoEntregaFallida() {
        this.estado = TipoEstado.ENTREGA_FALLIDA;
    }

    public EstadoEntregaFallida(EntidadBeneficiaria e, String justificacion) {
        this.justificacion = justificacion;
        this.entidadBeneficiaria=e;
    }

    public String getJustificacion() {
        return justificacion;
    }

    @Override
    public void siguiente(Donacion d) {
        EstadoDonacion nuevoEstado = new EstadoEnDeposito();
        nuevoEstado.setDate();
        d.setEstadoDonacion(nuevoEstado);
    }
}