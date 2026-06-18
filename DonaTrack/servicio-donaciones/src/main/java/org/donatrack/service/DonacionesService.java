package org.donatrack.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.donatrack.controller.dto.*;
import org.donatrack.repository.DonacionesRepository;
import org.donatrack.dominio.donante.*;
import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.donacion.*;
import org.donatrack.dominio.entidadBeneficiaria.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class DonacionesService {
    private DonacionesRepository donacionesRepository;
    private DonantesService donantesService;

    public DonacionesService(DonacionesRepository donacionesRepository, DonantesService donantesService) {
        this.donacionesRepository = donacionesRepository;
        this.donantesService = donantesService;
    } // o seteado post instanciacion en archivo dependencias

    public List<Donacion> obtenerDonaciones(FiltrosDonacionDTO filtrosDonacionDTO) {
        List<Donacion> donaciones = donacionesRepository.buscarConFiltros(
                filtrosDonacionDTO.getTipoEstado(),
                filtrosDonacionDTO.getFechaDesde(),
                filtrosDonacionDTO.getFechaHasta(),
                filtrosDonacionDTO.getSubcategoria());

        return donaciones;
    }

    public void registrarDonacion(CrearDonacionDTO nuevaDonacion) {
        Donante donante = this.donantesService.obtenerDonantePorId(nuevaDonacion.getDonanteId()).orElseThrow();

        Donacion donacionCompleta = new Donacion(donante, nuevaDonacion.getItems()); // despues manejarse con IdDonante probablemente

        List<Donacion> donacionesSegmentadas = this.segmentarDonacion(donacionCompleta);

        donacionesRepository.saveAll(donacionesSegmentadas);

        donacionesSegmentadas.forEach(d -> donante.agregarDonacionHistorica(d)); // de nuevo, puede que labure con IDS despues

        this.donantesService.guardarDonante(donante);

    }

    private List<Donacion> segmentarDonacion(Donacion donacionCompleta) {
        Donante donante = donacionCompleta.getDonante();
        List<Donacion> donacionesSegmentadas = new ArrayList<>();
        List<ItemBien> bienesDonacion = donacionCompleta.getItemBienes();

        for (ItemBien itemBien : bienesDonacion) {
            Subcategoria subcategoriaBien = itemBien.getBien().getSubcategoria();
            Donacion donacionDeSubcategoria = this.donacionDeSubcategoria(donacionesSegmentadas, subcategoriaBien);

            if (donacionesSegmentadas.isEmpty() || donacionDeSubcategoria == null) {
                Donacion donacionNueva = new Donacion(donante, itemBien, subcategoriaBien);
                donacionesSegmentadas.add(donacionNueva);
            } else {
                donacionDeSubcategoria.agregarItemBien(itemBien);
            }
        }

        return donacionesSegmentadas;
    }

    private Donacion donacionDeSubcategoria(List<Donacion> donaciones, Subcategoria subcategoria) {
        return donaciones.stream()
                .filter(d -> d.getItemBienes().stream().anyMatch(b -> b.getBien().getSubcategoria() == subcategoria))
                .findFirst()
                .orElse(null);
    }

    public void asignarDonacion(EntidadBeneficiaria entidadBeneficiaria, Donacion donacion) {

    }

}