package org.donatrack.model;
import java.time.YearMonth;
import java.util.*;

public class DonanteIncentivos {
    private List<Donacion> donaciones;
    private String nombreUsuario;
    public CategoriaDonante categoriaActual;
    private List<Insignia> insigniasGanadas = new ArrayList<>();

    public DonanteIncentivos(String nombreUsuario, String categoria) {
        this.nombreUsuario = nombreUsuario;
        this.categoriaActual = new Colaborador();
    }

    public void registrarActividad(Donacion nuevaDonacion) {
        this.donaciones.add(nuevaDonacion);        
        
        if (this.categoriaActual.completoTodasLasMisiones(this)) {
            CategoriaDonante siguiente = this.categoriaActual.getSiguienteCategoria();
            if (siguiente != null) {
                this.categoriaActual = siguiente;
            }
        }
    }

    public int CalcularTotalDonaciones(){
        return donaciones.size();
    }

    public int CalcularDonacionesDistintas(){
        // recorrer donaciones y mirar bienes !=
        return 0;
    }

    public int CalcularOrganizacionesAyudadas(){
        // recorrer donaciones y mirar entidades !=
        return 0;
    }

    public Integer CalcularRachaActual(){
        // calcular cuantos meses seguidos ha donado
        return 0;
    }

    public int CalcularImpactoAcumulado(){ //cant bienes donados
        int impacto = 0;
        for (Donacion d : donaciones) {
            impacto += d.getCantidadBienes(); 
        }
        return impacto;
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

    public void ganarInsignia(Insignia nuevaInsignia) {
        if (!this.insigniasGanadas.contains(nuevaInsignia)) {
            this.insigniasGanadas.add(nuevaInsignia);
        }
    }


}