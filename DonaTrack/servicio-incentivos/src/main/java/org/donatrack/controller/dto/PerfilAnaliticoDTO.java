package org.donatrack.controller.dto;
import java.util.Map;

public class PerfilAnaliticoDTO {
    public String nombreUsuario;
    public String categoriaActual;
    public List<Donacion> donaciones;
    public Integer evolucionDonacionesPorPeriodo; //-> ver como resulta la funcion, x ahora ??
    public Double comparacionMensual;
    public Integer totalOrganizacionesAyudadas;
    public Integer impactoAcumulado;
    public Integer posicionRanking;

    public PerfilAnaliticoDTO(String nombreUsuario, String cat, List<Donacion> don, Integer evol, Double comp, Integer totalOrg, Integer imp, Integer pos) {
        this.nombreUsuario = nombreUsuario;
        this.categoriaActual = cat;
        this.donaciones = don;
        this.evolucionDonacionesPorPeriodo = evol;
        this.comparacionMensual = comp;
        this.totalOrganizacionesAyudadas = totalOrg;
        this.impactoAcumulado = imp;
        this.posicionRanking = pos;
    }
}