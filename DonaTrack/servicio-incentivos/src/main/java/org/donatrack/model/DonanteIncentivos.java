package org.donatrack.model;

import org.donatrack.controller.dto.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import jakarta.persistence.*;

@Entity
@Table(name = "donantes_incentivos")
public class DonanteIncentivos {
    @Id
    private String nombreUsuario;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "donante_id")
    private List<DonacionDTO> donaciones = new ArrayList<>();

    @Transient // no guardar
    private CategoriaDonante categoriaActual;
    
    private String nombreCategoriaActual;

    @ManyToMany(cascade = CascadeType.ALL) //insignias se repiten en varios donantes
    private List<Insignia> insigniasGanadas = new ArrayList<>();

    public DonanteIncentivos() {}

    public DonanteIncentivos(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.categoriaActual = new Colaborador();
        this.nombreCategoriaActual = this.categoriaActual.GetNombre();
    }

    public ResultadoActividad RegistrarActividad(DonacionDTO nuevaDonacion) {
        this.donaciones.add(nuevaDonacion);

        CategoriaDonante categoria = this.GetCategoriaActual();
        List<Mision> misionesRecienCumplidas = new ArrayList<>();

        for (Mision mision : categoria.GetMisionesDelNivel()) {
            if (mision.EstaCumplidaPor(this) && !YaTieneInsignia(mision.GetInsigniaOtorgada())) {
                this.GanarInsignia(mision.GetInsigniaOtorgada());
                misionesRecienCumplidas.add(mision);
            }
        }

        String nuevaCategoria = null;
        if (categoria.CompletoTodasLasMisiones(this)) {
            CategoriaDonante siguiente = categoria.GetSiguienteCategoria();
            if (siguiente != null) {
                this.categoriaActual = siguiente;
                this.nombreCategoriaActual = siguiente.GetNombre();
                nuevaCategoria = siguiente.GetNombre();
            }
        }

        return new ResultadoActividad(misionesRecienCumplidas, nuevaCategoria);
    }

    public Integer CalcularTotalDonaciones(){
        return donaciones.size();
    }

    public Integer CalcularDonacionesDistintas(){
        List<String> categorias = new ArrayList<>();

        for (DonacionDTO d : donaciones) {
            if (!categorias.contains(d.GetSubcategoria())) {
                categorias.add(d.GetSubcategoria());
            }
        }
        return categorias.size();
    }

    public Integer CalcularOrganizacionesAyudadas(){
        List<String> organizaciones = new ArrayList<>();

        for (DonacionDTO d : donaciones) {
            if (!organizaciones.contains(d.GetOrganizacion())) {
                organizaciones.add(d.GetOrganizacion());
            }
        }

        return organizaciones.size();
    }

    public Integer CalcularRachaActual(){
        if (donaciones.isEmpty()) {
            return 0;
        }

        List<DonacionDTO> ordenadas = new ArrayList<>(donaciones);
        ordenadas.sort(Comparator.comparing(DonacionDTO::GetFechaIngreso));

        Integer racha = 1;
        YearMonth mesAnterior = YearMonth.from(ordenadas.get(0).GetFechaIngreso());

        for (Integer i = 1; i < donaciones.size(); i++) {
            YearMonth mesActual = YearMonth.from(ordenadas.get(i).GetFechaIngreso());

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

    public Integer GetCantidadBienesDonados(){
        Integer total = 0;
        for (DonacionDTO d : donaciones) {
            total += d.GetCantidadBienes();
        }
        return total;
    }

    public Integer CalcularImpactoAcumulado(){ //cant bienes donados
        Integer impacto = this.GetCantidadBienesDonados();
        return impacto;
    }

    public Integer CalcularTotalDonadoEntre(LocalDate fechaInicio, LocalDate fechaFin) {
        Integer total = 0;

        for (DonacionDTO d : donaciones) {
            LocalDate fecha = d.GetFechaIngreso();

            if (!fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin)) {
                total += d.GetCantidadBienes();
            }
        }

        return total;
    }

    public Double ObtenerComparacionMensual(YearMonth mesActual, YearMonth mesAnterior) {
        Double totalActual = 0.0;
        Double totalAnterior = 0.0;

        for (DonacionDTO d : donaciones) {
            YearMonth periodo = YearMonth.from(d.GetFechaIngreso());

            if (periodo.equals(mesActual)) {
                totalActual += d.GetCantidadBienes();
            }

            if (periodo.equals(mesAnterior)) {
                totalAnterior += d.GetCantidadBienes();
            }
        }
        if (totalAnterior == 0) {
            return 0.0;
        }
        return ((totalActual - totalAnterior) / totalAnterior) * 100;
    }

    public List<Integer> ObtenerEvolucionDonacionesPorPeriodo(YearMonth mesInicio, YearMonth mesFinal) {
        List<Integer> cantidadBienesPorMes = new ArrayList<>();

        YearMonth mesActual = mesInicio;

        while (!mesActual.isAfter(mesFinal)) {
            int totalMes = 0;

            for (DonacionDTO d : donaciones) {
                if (YearMonth.from(d.GetFechaIngreso()).equals(mesActual)) {
                    totalMes += d.GetCantidadBienes();
                }
            }

            cantidadBienesPorMes.add(totalMes);
            mesActual = mesActual.plusMonths(1);
        }

        return cantidadBienesPorMes;
    }

    public void GanarInsignia(Insignia nuevaInsignia) {
        if (!this.insigniasGanadas.contains(nuevaInsignia)) {
            this.insigniasGanadas.add(nuevaInsignia);
        }
    }

    private Boolean YaTieneInsignia(Insignia insignia) {
        return this.insigniasGanadas.contains(insignia);
    }

    public CategoriaDonante GetCategoriaActual() {
        if(categoriaActual == null){
            switch(nombreCategoriaActual){
                case "Colaborador":
                    categoriaActual = new Colaborador();
                    break;
                case "Sostenedor":
                    categoriaActual = new Sostenedor();
                    break;
                case "Transformador":
                    categoriaActual = new Transformador();
                    break;
            }
        }
        return categoriaActual;
    }

    public Integer CalcularMisionesCumplidasEn(YearMonth mes){
        LocalDate finDeMes = mes.atEndOfMonth();
        LocalDate finDeMesAnterior = mes.minusMonths(1).atEndOfMonth();
 
        return ContarMisionesNuevasEntre(finDeMesAnterior, finDeMes);
    }

    private Integer ContarMisionesNuevasEntre(LocalDate fechaInicio, LocalDate fechaFin) {
        DonanteIncentivos snapshotInicio = CrearSnapshotHasta(fechaInicio);
        DonanteIncentivos snapshotFin = CrearSnapshotHasta(fechaFin);
 
        int nuevas = 0;
        CategoriaDonante categoria = new Colaborador();
 
        while (categoria != null) {
            for (Mision mision : categoria.GetMisionesDelNivel()) {
                boolean estabaCumplidaAntes = mision.EstaCumplidaPor(snapshotInicio);
                boolean estaCumplidaAhora = mision.EstaCumplidaPor(snapshotFin);
                if (!estabaCumplidaAntes && estaCumplidaAhora) {
                    nuevas++;
                }
            }
 
            if (categoria.CompletoTodasLasMisiones(snapshotFin)) {
                categoria = categoria.GetSiguienteCategoria();
            } else {
                categoria = null;
            }
        }
 
        return nuevas;
    }

    private DonanteIncentivos CrearSnapshotHasta(LocalDate fechaLimite) {
        List<DonacionDTO> filtradas = new ArrayList<>();
        for (DonacionDTO d : this.donaciones) {
            if (!d.GetFechaIngreso().isAfter(fechaLimite)) {
                filtradas.add(d);
            }
        }
 
        DonanteIncentivos snapshot = new DonanteIncentivos(this.nombreUsuario);
        snapshot.donaciones = filtradas;
        return snapshot;
    }

    public YearMonth GetMesPrimeraDonacion(){
        if (donaciones.isEmpty()) {
            return YearMonth.now();
        }
 
        LocalDate fechaMasAntigua = donaciones.get(0).GetFechaIngreso();
        for (DonacionDTO d : donaciones) {
            if (d.GetFechaIngreso().isBefore(fechaMasAntigua)) {
                fechaMasAntigua = d.GetFechaIngreso();
            }
        }
        return YearMonth.from(fechaMasAntigua);
    }


    public String GetNombreCategoriaActual() { return nombreCategoriaActual; }

    public String GetNombreUsuario() { return nombreUsuario; }

    public List<DonacionDTO> GetDonaciones() { return donaciones; }

    public List<Insignia> GetInsigniasGanadas() { return insigniasGanadas; }
}