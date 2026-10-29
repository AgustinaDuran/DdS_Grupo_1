package org.donatrack.model;

import java.time.LocalDateTime;

// subdocumento: vive embebido en el historial de la entrega
public class CambioEstado {
    private EstadoEntrega estado;
    private String motivo;
    private LocalDateTime fecha = LocalDateTime.now();

    public CambioEstado() {}

    public CambioEstado(EstadoEntrega estado, String motivo) {
        this.estado = estado;
        this.motivo = motivo;
    }

    public EstadoEntrega getEstado() { return estado; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getFecha() { return fecha; }
}
