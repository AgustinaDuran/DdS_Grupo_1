package org.donatrack.model;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class DonanteIncentivos {
    private String nombre;
    private List<Donacion> donaciones = new ArrayList<>();
    private String nombreUsuario;
    public CategoriaDonante categoriaActual;
    private List<Insignia> insigniasGanadas = new ArrayList<>();

    public DonanteIncentivos(String nombreUsuario, String categoria) {
        this.nombreUsuario = nombreUsuario;
        this.categoriaActual = new Colaborador();
    }

    public void registrarActividad(Donacion nuevaDonacion) { //agregar en donante que al agregar donacion, se actualice aca
        this.donaciones.add(nuevaDonacion);        
        
        if (this.categoriaActual.completoTodasLasMisiones(this)) {
            CategoriaDonante siguiente = this.categoriaActual.getSiguienteCategoria();
            if (siguiente != null) {
                this.categoriaActual = siguiente;
            }
        }
    }

    public Integer CalcularTotalDonaciones(){
        return donaciones.size();
    }

    public Integer CalcularDonacionesDistintas(){
        List<Subcategoria> categorias = new ArrayList<>();
        for (Donacion d : donaciones) {
            if (!categorias.contains(d.getSubcategoria())) {
                categorias.add(d.getSubcategoria());
            }
        }
        return categorias.size();
    }

    public Integer CalcularOrganizacionesAyudadas(){
        List<Organizacion> organizaciones = new ArrayList<>();
        for (Donacion d : donaciones) {
            if (!organizaciones.contains(d.getOrganizacion())) { //agregar a quien se dono en donacion
                organizaciones.add(d.getOrganizacion());
            }
        }
        return organizaciones.size();
    }

    public Integer CalcularRachaActual(){
        if (donaciones.isEmpty()) {
            return 0;
        }

        int racha = 1;
        YearMonth mesAnterior = YearMonth.from(donaciones.get(0).getFechaIngreso());

        for (int i = 1; i < donaciones.size(); i++) {
            YearMonth mesActual = YearMonth.from(donaciones.get(i).getFechaIngreso());

            if (mesActual.equals(mesAnterior)) {
                continue;
            }

            if (mesAnterior.plusMonths(1).equals(mesActual)) {
                racha++;
            } else {
                racha = 1;
            }

            mesAnterior = mesActual;
        }
        return racha;
    }

    public Integer CalcularImpactoAcumulado(){ //cant bienes donados
        Integer impacto = donante.getCantidadTotal();
        return impacto;
    }

    public Integer calcularTotalDonadoEntre(LocalDate fechaInicio, LocalDate fechaFin) {
        Integer total = 0;

        for (Donacion d : donaciones) {
            LocalDate fecha = d.getFechaIngreso();
            if (!fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin)) {
                total += d.getCantidadBienes();
            }
        }

        return total;
    }

    public Double ObtenerComparacionMensual(YearMonth mesActual, YearMonth mesAnterior) {
        Double totalActual = 0.0;
        Double totalAnterior = 0.0;

        for (Donacion d : donaciones) {
            YearMonth periodo = YearMonth.from(d.getFechaIngreso());

            if (periodo.equals(mesActual)) {
                totalActual += d.getCantidad();
            }

            if (periodo.equals(mesAnterior)) {
                totalAnterior += d.getCantidad();
            }
        }
        if (totalAnterior == 0) {
            return 0.0;
        }
        return ((totalActual - totalAnterior) / totalAnterior) * 100;
    }

    public Integer ObtenerEvolucionDonacionesPorPeriodo() {
        //COMO SE CALCULA?
        return 0;
    }

    public void ganarInsignia(Insignia nuevaInsignia) {
        if (!this.insigniasGanadas.contains(nuevaInsignia)) {
            this.insigniasGanadas.add(nuevaInsignia);
        }
    }


}