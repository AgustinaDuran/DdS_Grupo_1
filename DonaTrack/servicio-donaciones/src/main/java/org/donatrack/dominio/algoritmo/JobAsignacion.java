package org.donatrack.dominio.algoritmo;

public class JobAsignacion {

    private CoordinadorAsignacion servicioAsignacion;

    public JobAsignacion(CoordinadorAsignacion servicioAsignacion) {
        this.servicioAsignacion = servicioAsignacion;
    }

    public void ejecutarAsignaciones() {
        servicioAsignacion.generarRecomendaciones();
    }
}