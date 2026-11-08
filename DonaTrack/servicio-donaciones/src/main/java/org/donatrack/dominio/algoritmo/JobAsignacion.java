package org.donatrack.dominio.algoritmo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// Corre los algoritmos de asignación en horario de baja carga (por defecto, todos los días a las 03:00).
@Component
public class JobAsignacion {

    private static final Logger log = LoggerFactory.getLogger(JobAsignacion.class);

    private final CoordinadorAsignacion coordinadorAsignacion;

    public JobAsignacion(CoordinadorAsignacion coordinadorAsignacion) {
        this.coordinadorAsignacion = coordinadorAsignacion;
    }

    @Scheduled(cron = "${donaciones.asignacion.cron:0 0 3 * * ?}")
    public void ejecutarAsignaciones() {
        log.info("Inicio del cálculo de rankings de asignación");
        int procesadas = coordinadorAsignacion.generarRecomendaciones().size();
        log.info("Fin del cálculo de rankings de asignación: {} donaciones procesadas", procesadas);
    }
}
