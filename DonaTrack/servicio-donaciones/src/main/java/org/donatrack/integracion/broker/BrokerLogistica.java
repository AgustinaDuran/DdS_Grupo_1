package org.donatrack.integracion.broker;

import org.donatrack.integracion.dto.DepositoRequest;
import org.donatrack.integracion.dto.EntregaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Broker de integración con Logística: Donaciones sólo habla con el broker y el broker decide
 * a qué proveedor de logística enviar cada pedido.
 * - registrarEntregas: failover. Intenta con los proveedores en orden (@Order de cada adapter)
 *   y se queda con el primero que responde.
 * - listarEntregas: consulta a todos los proveedores, porque por el failover una entrega puede
 *   haber quedado en cualquiera de ellos.
 */
@Component
public class BrokerLogistica {

    private static final Logger log = LoggerFactory.getLogger(BrokerLogistica.class);

    private final List<ProveedorLogistica> proveedores;

    public BrokerLogistica(List<ProveedorLogistica> proveedores) {
        this.proveedores = proveedores;
    }

    public void registrarEntregas(DepositoRequest request) {
        for (ProveedorLogistica p : proveedores) {
            try {
                p.registrarEntregas(request);
                log.info("Se registró en el proveedor {}", p.nombre());
                return;
            } catch (Exception e) {
                log.warn("Falló el proveedor {}, se prueba el siguiente: {}", p.nombre(), e.getMessage());
            }
        }
        log.error("Ningún proveedor de logística pudo registrar las entregas.");
    }

    /**
     * Devuelve las entregas de todos los proveedores. Si uno no responde se omite y sus entregas
     * aparecen en el próximo ciclo de polling.
     */
    public List<EntregaResponse> listarEntregas() {
        List<EntregaResponse> entregas = new ArrayList<>();
        for (ProveedorLogistica p : proveedores) {
            try {
                entregas.addAll(p.listarEntregas());
            } catch (Exception e) {
                log.warn("No se pudieron consultar las entregas del proveedor {}: {}", p.nombre(), e.getMessage());
            }
        }
        return entregas;
    }
}
