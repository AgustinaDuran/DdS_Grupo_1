package org.donatrack.integracion.broker;

import org.donatrack.integracion.dto.DepositoRequest;
import org.donatrack.integracion.dto.EntregaResponse;

import java.util.List;

/**
 * Contrato que cumple cada proveedor de logística para que el BrokerLogistica lo pueda usar.
 * Cada implementación es un adapter que traduce entre el formato de DonaTrack y la API de su
 * proveedor.
 */
public interface ProveedorLogistica {

    /**
     * Debe lanzar una excepción si el proveedor no pudo registrar las entregas: de eso depende
     * el failover del broker.
     */
    void registrarEntregas(DepositoRequest request);

    /** Devuelve las entregas del proveedor ya traducidas al formato de DonaTrack. */
    List<EntregaResponse> listarEntregas();

    /** Nombre para los logs del broker. */
    String nombre();
}
