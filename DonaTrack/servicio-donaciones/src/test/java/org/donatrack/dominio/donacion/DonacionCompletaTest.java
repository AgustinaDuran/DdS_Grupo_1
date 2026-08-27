package org.donatrack.dominio.donacion;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.donatrack.dominio.bien.Bien;
import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.dominio.categoria.Categoria;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.junit.jupiter.api.Test;

class DonacionCompletaTest {

    @Test
    void segmentaPorSubcategoriaYConservaLasCantidades() {
        Donante donante = new DonantePrueba();
        Subcategoria alimentos = new Subcategoria("Alimentos", new Categoria("Comestibles"));
        Subcategoria abrigo = new Subcategoria("Abrigo", new Categoria("Vestimenta"));

        ItemBien arroz = new ItemBien(new BienPrueba("Arroz", alimentos), 10);
        ItemBien fideos = new ItemBien(new BienPrueba("Fideos", alimentos), 5);
        ItemBien campera = new ItemBien(new BienPrueba("Campera", abrigo), 2);

        DonacionCompleta original = new DonacionCompleta(
                donante, List.of(arroz, fideos, campera), "Donacion de prueba");

        List<Donacion> segmentadas = original.segmentarDonacion();

        assertThat(segmentadas).hasSize(2);
        assertThat(segmentadas).allMatch(donacion -> donacion.getDonante() == donante);
        assertThat(segmentadas)
                .extracting(Donacion::getCantidadTotal)
                .containsExactlyInAnyOrder(15, 2);
        assertThat(segmentadas)
                .extracting(Donacion::getSubcategoria)
                .containsExactlyInAnyOrder(alimentos, abrigo);
    }

    private static class BienPrueba extends Bien {
        BienPrueba(String nombre, Subcategoria subcategoria) {
            super(nombre, nombre, subcategoria);
        }
    }

    private static class DonantePrueba extends Donante {
        DonantePrueba() {
            super(new Organizacion("Organizacion de prueba", "30-00000000-0"));
        }
    }
}