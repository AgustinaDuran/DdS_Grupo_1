package org.donatrack.dominio.donacion;
import java.time.LocalDateTime;

import org.donatrack.dominio.bien.TipoUnidad;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

public abstract class EstadoDonacion {
    private LocalDateTime fechaIngresoEstado;
    protected TipoEstado estado;

    public TipoEstado getEstado() {
        return estado;
    }

    public LocalDateTime getDate(){
        return fechaIngresoEstado;
    }

    public void setDate(){
        fechaIngresoEstado= LocalDateTime.now();
    }
    public void siguiente(Donacion d){
        throw new RuntimeException("Este estado no posee un estado siguiente");
    }



    public void falloEnEstado(Donacion d, String justificacion){
        throw new RuntimeException("No posee un fallo de estado");
    }
    /* 
    public void asignar(Donacion d) {
        throw new RuntimeException("No se puede asignar la donación en el estado actual.");
    }

    
    public void planificarRuta(Donacion d) {
        throw new RuntimeException("No se puede planificar ruta en el estado actual.");
    }
    
    public void iniciarTraslado(Donacion d) {
        throw new RuntimeException("No se puede iniciar el traslado en el estado actual.");
    }
    
    public void confirmarEntrega(Donacion d) {
        throw new RuntimeException("No se puede confirmar la entrega en el estado actual.");
    }
    
    public void registrarEntregaFallida(Donacion d, String justificacion) {
        throw new RuntimeException("No se puede registrar una entrega fallida en el estado actual.");
    }
    
    public void marcarComoVencida(Donacion d) {
        throw new RuntimeException("No se puede vencer la donación en el estado actual.");
    }
    
    public void volverADeposito(Donacion d) {
        throw new RuntimeException("No se puede volver al depósito en el estado actual.");
    }
    */
    

}

