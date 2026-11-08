package org.donatrack.controller.dto.Asignaciones;

public enum EstadoRanking {
    // El job ya calculó el ranking de la donación.
    CALCULADO,
    // La donación entró después del último cálculo: tendrá ranking en la próxima ejecución.
    PENDIENTE
}
