package org.donatrack.controller.dto;

import org.donatrack.model.Camion;
import org.donatrack.model.Entrega;
import java.util.List;

public class SolicitudOptimizadorDTO {
    private List<CamionDisponibleDTO> camionesDisponibles;
    private List<EntregaAPlanificarDTO> entregas;
    private String urlCallback;

    public SolicitudOptimizadorDTO(List<Camion> camiones, List<Entrega> entregas, String urlCallback) {
        this.camionesDisponibles = camiones.stream()
                .map(c -> new CamionDisponibleDTO(c.getPatente(), c.getCapacidadVolumen(), c.getAltura(), c.getCapacidadCarga()))
                .toList();
        this.entregas = entregas.stream()
                .map(e -> new EntregaAPlanificarDTO(e.getId(), e.getDireccionDestino()))
                .toList();
        this.urlCallback = urlCallback;
    }

    public List<CamionDisponibleDTO> getCamionesDisponibles() { return camionesDisponibles; }
    public List<EntregaAPlanificarDTO> getEntregas() { return entregas; }
    public String getUrlCallback() { return urlCallback; }

    public static class CamionDisponibleDTO {
        public String patente;
        public Float capacidadVolumen;
        public Float altura;
        public Float capacidadCarga;

        public CamionDisponibleDTO(String patente, Float capacidadVolumen, Float altura, Float capacidadCarga) {
            this.patente = patente;
            this.capacidadVolumen = capacidadVolumen;
            this.altura = altura;
            this.capacidadCarga = capacidadCarga;
        }
    }

    public static class EntregaAPlanificarDTO {
        public Long entregaId;
        public String direccionDestino;

        public EntregaAPlanificarDTO(Long entregaId, String direccionDestino) {
            this.entregaId = entregaId;
            this.direccionDestino = direccionDestino;
        }
    }
}