package org.donatrack.controller.dto;

import java.util.List;

public class PodioMensualDTO {
    public String periodoMes;
    public List<PuestoGanador> losDestacados;

    public PodioMensualDTO(String periodoMes, List<PuestoGanador> losDestacados) {
        this.periodoMes = periodoMes;
        this.losDestacados = losDestacados;
    }

    public static class PuestoGanador {
        public Integer puesto;
        public String nombreUsuario;
        public Integer misionesLogradas;

        public PuestoGanador(Integer puesto, String nombreUsuario, Integer misionesLogradas) {
            this.puesto = puesto;
            this.nombreUsuario = nombreUsuario;
            this.misionesLogradas = misionesLogradas;
        }
    }
}