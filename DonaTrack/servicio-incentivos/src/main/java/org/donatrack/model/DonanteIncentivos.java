package org.donatrack.model;
import java.time.YearMonth;
import java.util.*;

public class DonanteIncentivos {
    private List<Donacion> donaciones = new ArrayList<>();
    private String nombreUsuario;


    public int CalcularTotalDonaciones(){
        return donaciones.size();
    }

    public int CalcularOrganizacionesAyudadas(){
        // recorrer donaciones y mirar entidades !=
        return 0;
    }

    public int CalcularImpactoAcumulado(){
        // ???
        return 0;
    }

    public double calcularTotalDonadoEn(YearMonth periodo) {
        double total = 0;

        for (Donacion d : donaciones) {
            if (YearMonth.from(d.getFecha()).equals(periodo)) {
                total += d.getCantidadBienes();
            }
        }

        return total;
    }

    public double ObtenerEvolucionDonacionesPorPeriodo(YearMonth mesActual, YearMonth mesAnterior) {
        double totalActual = calcularTotalDonadoEn(mesActual);
        double totalAnterior = calcularTotalDonadoEn(mesAnterior);

        if (totalAnterior == 0) return totalActual > 0 ? 100.0 : 0.0;
        return ((totalActual - totalAnterior) / totalAnterior) * 100;
    }
    public int ObtenerComparacionMensual() {
        //comparar con el mes anterior, devolver porcentaje
        return 0;
    }


}