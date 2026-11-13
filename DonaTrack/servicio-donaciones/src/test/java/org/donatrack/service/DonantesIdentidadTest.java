package org.donatrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.donatrack.config.IdentidadAutenticada;
import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.donante.DonanteJuridico;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.integracion.DestinatarioResolver;
import org.donatrack.integracion.KeycloakUserClient;
import org.donatrack.integracion.NotificacionesClient;
import org.donatrack.repository.DonantesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

class DonantesIdentidadTest {

    private DonantesRepository repositorio;
    private DonantesService servicio;

    @BeforeEach
    void preparar() {
        repositorio = mock(DonantesRepository.class);
        when(repositorio.save(any(Donante.class))).thenAnswer(invocacion -> invocacion.getArgument(0));
        servicio = new DonantesService(repositorio, mock(UsuariosService.class), null,
                mock(KeycloakUserClient.class), mock(NotificacionesClient.class),
                mock(DestinatarioResolver.class), "http://localhost:3000");
    }

    private static Donante donanteConEmail(String email) {
        Organizacion datos = new Organizacion("Arcos Plateados", "30-123");
        datos.agregarContacto(new Contacto("MAIL", email));
        return new DonanteJuridico(datos, null, "Gastronomía");
    }

    @Test
    void devuelveElDonanteYaVinculadoSinBuscarPorEmail() {
        Donante vinculado = donanteConEmail("ana@mail.com");
        vinculado.setKeycloakUserId("sub-1");
        when(repositorio.findByKeycloakUserId("sub-1")).thenReturn(Optional.of(vinculado));

        assertThat(servicio.obtenerPorIdentidad("sub-1", "otro@mail.com")).contains(vinculado);
        verify(repositorio, never()).findAll();
    }

    @Test
    void vinculaPorEmailVerificadoAUnDonanteSinCuenta() {
        Donante importado = donanteConEmail("ana@mail.com");
        when(repositorio.findByKeycloakUserId("sub-google")).thenReturn(Optional.empty());
        when(repositorio.findAll()).thenReturn(List.of(importado));

        Optional<Donante> resultado = servicio.obtenerPorIdentidad("sub-google", "ANA@mail.com");

        assertThat(resultado).contains(importado);
        assertThat(importado.getKeycloakUserId()).isEqualTo("sub-google");
    }

    @Test
    void noVinculaSiElEmailNoEstaVerificado() {
        when(repositorio.findByKeycloakUserId("sub-x")).thenReturn(Optional.empty());

        assertThat(servicio.obtenerPorIdentidad("sub-x", null)).isEmpty();
        verify(repositorio, never()).findAll();
    }

    @Test
    void noPisaElVinculoDeOtraCuenta() {
        Donante deOtraCuenta = donanteConEmail("ana@mail.com");
        deOtraCuenta.setKeycloakUserId("sub-original");
        when(repositorio.findByKeycloakUserId("sub-intruso")).thenReturn(Optional.empty());
        when(repositorio.findAll()).thenReturn(List.of(deOtraCuenta));

        assertThat(servicio.obtenerPorIdentidad("sub-intruso", "ana@mail.com")).isEmpty();
        assertThat(deOtraCuenta.getKeycloakUserId()).isEqualTo("sub-original");
    }

    @Test
    void generaContrasenasTemporalesValidas() {
        String contrasena = DonantesService.generarContrasenaTemporal();
        assertThat(contrasena).hasSize(12).matches("[A-Za-z0-9]+");
        assertThat(DonantesService.generarContrasenaTemporal()).isNotEqualTo(contrasena);
    }

    private static JwtAuthenticationToken token(String sub, Boolean emailVerificado, String... roles) {
        Jwt jwt = Jwt.withTokenValue("t").header("alg", "none").subject(sub)
                .claims(claims -> {
                    claims.put("email", "ana@mail.com");
                    if (emailVerificado != null) claims.put("email_verified", emailVerificado);
                })
                .issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(60)).build();
        return new JwtAuthenticationToken(jwt,
                java.util.Arrays.stream(roles).map(rol -> new SimpleGrantedAuthority("ROLE_" + rol)).toList());
    }

    @Test
    void soloLaPropiaCuentaOAdminPuedenConsultarUnaIdentidad() {
        assertThat(IdentidadAutenticada.puedeConsultar(token("sub-1", true, "DONANTE"), "sub-1")).isTrue();
        assertThat(IdentidadAutenticada.puedeConsultar(token("sub-1", true, "DONANTE"), "sub-2")).isFalse();
        assertThat(IdentidadAutenticada.puedeConsultar(token("sub-admin", true, "ADMIN"), "sub-2")).isTrue();
        assertThat(IdentidadAutenticada.puedeConsultar(null, "sub-2")).isFalse();
    }

    @Test
    void soloUsaElEmailCuandoKeycloakLoMarcaVerificado() {
        assertThat(IdentidadAutenticada.emailVerificado(token("s", true))).isEqualTo("ana@mail.com");
        assertThat(IdentidadAutenticada.emailVerificado(token("s", false))).isNull();
        assertThat(IdentidadAutenticada.emailVerificado(token("s", null))).isNull();
    }
}
