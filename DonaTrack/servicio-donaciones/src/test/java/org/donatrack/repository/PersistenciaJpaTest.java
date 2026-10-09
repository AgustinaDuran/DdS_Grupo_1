package org.donatrack.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.donatrack.dominio.bien.BienEstado;
import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.dominio.categoria.Categoria;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donante.DonantePersona;
import org.donatrack.dominio.usuario.persona.Persona;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;

@DataJpaTest
class PersistenciaJpaTest {
    @Autowired
    private EntityManager entityManager;

    @Test
    void persisteUnaDonacionConSusRelaciones() {
        Categoria categoria = new Categoria("Alimentos");
        Subcategoria subcategoria = new Subcategoria("No perecederos", categoria);
        Persona persona = new Persona("Ana", "Perez", 30, "12345678", "F", "Calle 1",
                new Contacto("MAIL", "ana@ejemplo.com"));
        persona.agregarContacto(persona.getContactoPredeterminado());
        DonantePersona donante = new DonantePersona(persona);
        BienEstado bien = new BienEstado("Lata", "Conserva", subcategoria, true);

        entityManager.persist(categoria);
        entityManager.persist(subcategoria);
        entityManager.persist(persona);
        entityManager.persist(donante);
        entityManager.persist(bien);

        Donacion donacion = new Donacion(donante, new ItemBien(bien, 2), subcategoria);
        entityManager.persist(donacion);
        entityManager.flush();
        entityManager.clear();

        Donacion guardada = entityManager.find(Donacion.class, donacion.getId());

        assertThat(guardada.getDonante().getId()).isEqualTo(donante.getId());
        assertThat(guardada.getItemBienes()).hasSize(1);
        assertThat(guardada.getEstadoDonacion().getEstado().name()).isEqualTo("EN_DEPOSITO");
    }
}
