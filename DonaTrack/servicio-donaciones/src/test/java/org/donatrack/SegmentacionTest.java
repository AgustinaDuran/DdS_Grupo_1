package org.donatrack;

import org.donatrack.controller.dto.CrearDonacionDTO;
import org.donatrack.dominio.bien.Bien;
import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.repository.DonacionesRepository;
import org.donatrack.service.DonacionesService;
import org.donatrack.service.DonantesService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifica la segmentación: una carga masiva de bienes se divide en
 * donaciones independientes, una por subcategoría, agrupando los ítems
 * de la misma subcategoría en una sola donación.
 */
class SegmentacionTest {

    private ItemBien itemDeSubcategoria(Subcategoria sub, int cantidad) {
        Bien bien = mock(Bien.class);
        when(bien.getSubcategoria()).thenReturn(sub);
        ItemBien item = mock(ItemBien.class);
        when(item.getBien()).thenReturn(bien);
        when(item.getCantidad()).thenReturn(cantidad);
        return item;
    }

    @Test
    @SuppressWarnings("unchecked")
    void segmentaUnaDonacionPorSubcategoria() {
        Subcategoria arroz = new Subcategoria("Arroz", null);
        Subcategoria fideos = new Subcategoria("Fideos", null);

        ItemBien arroz1 = itemDeSubcategoria(arroz, 5);
        ItemBien arroz2 = itemDeSubcategoria(arroz, 3);
        ItemBien fideos1 = itemDeSubcategoria(fideos, 10);

        CrearDonacionDTO dto = new CrearDonacionDTO();
        dto.setDonanteId(1L);
        dto.setItems(List.of(arroz1, arroz2, fideos1));

        DonacionesRepository repo = mock(DonacionesRepository.class);
        DonantesService donantesService = mock(DonantesService.class);
        Donante donante = mock(Donante.class);
        when(donantesService.obtenerDonantePorId(1L)).thenReturn(Optional.of(donante));

        DonacionesService service = new DonacionesService(repo, donantesService);
        service.registrarDonacion(dto);

        ArgumentCaptor<List<Donacion>> captor = ArgumentCaptor.forClass(List.class);
        verify(repo).saveAll(captor.capture());
        List<Donacion> segmentadas = captor.getValue();

        // Dos subcategorías distintas -> dos donaciones resultantes.
        assertEquals(2, segmentadas.size());

        // Los dos ítems de arroz quedan agrupados en una única donación (cantidad total 8).
        Donacion donArroz = segmentadas.stream()
                .filter(d -> d.getSubcategoria() == arroz)
                .findFirst()
                .orElseThrow();
        assertEquals(2, donArroz.getItemBienes().size());
        assertEquals(8, donArroz.getCantidadTotal());

        Donacion donFideos = segmentadas.stream()
                .filter(d -> d.getSubcategoria() == fideos)
                .findFirst()
                .orElseThrow();
        assertEquals(1, donFideos.getItemBienes().size());
        assertEquals(10, donFideos.getCantidadTotal());
    }
}
