package org.donatrack.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.donatrack.model.Notificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotificacionesRepositoryTest {

    private NotificacionesRepository repositorio;

    @BeforeEach
    void inicializar() {
        repositorio = new NotificacionesRepository();
    }

    private Notificacion nueva(String destino) {
        return new Notificacion(destino, "Alguien", "un mensaje", null);
    }

    @Test
    @DisplayName("Guarda y devuelve las notificaciones")
    void guardaYLista() {
        repositorio.save(nueva("a@b.com"));
        repositorio.save(nueva("c@d.com"));

        assertThat(repositorio.findAll())
                .hasSize(2)
                .extracting(Notificacion::getDestinatario)
                .containsExactly("a@b.com", "c@d.com");
    }

    @Test
    @DisplayName("findById funciona con ids mayores a 127 (regresion: comparaba Long con ==)")
    void encuentraPorIdFueraDelCacheDeLong() {
        // Los Long entre -128 y 127 estan cacheados por la JVM, asi que un "=="
        // accidental funcionaria para ids chicos y fallaria para los grandes.
        Notificacion ultima = null;
        for (int i = 0; i < 130; i++) {
            ultima = nueva("destino" + i + "@ejemplo.com");
            repositorio.save(ultima);
        }

        assertThat(ultima.getId()).isGreaterThan(127L);
        assertThat(repositorio.findById(ultima.getId())).isSameAs(ultima);
    }

    @Test
    @DisplayName("findById devuelve null si no existe")
    void devuelveNullSiNoExiste() {
        assertThat(repositorio.findById(999999L)).isNull();
    }

    @Test
    @DisplayName("Se puede guardar despues de borrar (regresion: la lista quedaba inmutable)")
    void permiteGuardarDespuesDeBorrar() {
        Notificacion primera = nueva("a@b.com");
        repositorio.save(primera);

        repositorio.delete(primera.getId());

        assertThat(repositorio.findAll()).isEmpty();
        // Antes esto explotaba con UnsupportedOperationException porque delete()
        // reasignaba la lista al resultado inmutable de .toList()
        assertThatCode(() -> repositorio.save(nueva("c@d.com"))).doesNotThrowAnyException();
        assertThat(repositorio.findAll()).hasSize(1);
    }
}
