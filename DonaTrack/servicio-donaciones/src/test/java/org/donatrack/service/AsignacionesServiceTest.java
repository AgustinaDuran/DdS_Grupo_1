package org.donatrack.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.donatrack.controller.dto.PaginaDTO;
import org.donatrack.controller.dto.Asignaciones.DonacionPendienteDTO;
import org.donatrack.controller.dto.Asignaciones.EstadoRanking;
import org.donatrack.controller.dto.Asignaciones.RankingDonacionDTO;
import org.donatrack.controller.dto.Donaciones.DonacionDTO;
import org.donatrack.dominio.algoritmo.AlgoritmoAsignacion;
import org.donatrack.dominio.algoritmo.CoordinadorAsignacion;
import org.donatrack.dominio.algoritmo.ResultadoAsignacion;
import org.donatrack.dominio.bien.BienEstado;
import org.donatrack.dominio.bien.ItemBien;
import org.donatrack.dominio.categoria.Categoria;
import org.donatrack.dominio.categoria.Subcategoria;
import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.TipoEstado;
import org.donatrack.dominio.donante.DonantePersona;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.entidadBeneficiaria.TipoEntidadBeneficiaria;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.dominio.usuario.persona.Persona;
import org.donatrack.repository.DonacionesRepository;
import org.donatrack.repository.EntidadesBeneficiariasRepository;
import org.donatrack.repository.ResultadoAsignacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;

// Cálculo del ranking (fase 3) y consultas del flujo de asignación (fase 4) contra una base H2.
@DataJpaTest
class AsignacionesServiceTest {

    @Autowired private EntityManager em;
    @Autowired private DonacionesRepository donacionesRepository;
    @Autowired private ResultadoAsignacionRepository resultadosRepository;
    @Autowired private EntidadesBeneficiariasRepository entidadesRepository;

    private CoordinadorAsignacion coordinador;
    private AsignacionesService service;

    private DonantePersona donante;
    private Subcategoria subcategoria;
    private EntidadBeneficiaria a, b, c;

    @BeforeEach
    void preparar() {
        Categoria categoria = new Categoria("Mobiliario");
        subcategoria = new Subcategoria("Sillas", categoria);
        Persona persona = new Persona("Ana", "Perez", 30, "12345678", "F", "Calle 1",
                new Contacto("MAIL", "ana@ejemplo.com"));
        persona.agregarContacto(persona.getContactoPredeterminado());
        donante = new DonantePersona(persona);
        em.persist(categoria);
        em.persist(subcategoria);
        em.persist(persona);
        em.persist(donante);

        a = entidad("Hogar A");
        b = entidad("Comedor B");
        c = entidad("Escuela C");

        // Algoritmo 1: [A, B, C]. Algoritmo 2: [C, B]. Coincidencias: B (2+2=4) y C (3+1=4).
        // Empatan en la suma, así que desempata el id: B primero.
        coordinador = new CoordinadorAsignacion(
                List.of(algoritmoFijo("ALGO_1", a, b, c), algoritmoFijo("ALGO_2", c, b)),
                entidadesRepository, donacionesRepository, resultadosRepository);
        service = new AsignacionesService(coordinador, donacionesRepository, resultadosRepository, entidadesRepository);
    }

    @Test
    void listaPendientesConRankingCalculadoYPendiente() {
        Donacion conRanking = donacion();
        coordinador.generarRecomendaciones();
        Donacion sinRanking = donacion(); // entra después del cálculo
        reiniciarContexto();

        PaginaDTO<DonacionPendienteDTO> pagina = service.obtenerPendientes(0, 20, null);

        assertThat(pagina.totalElementos()).isEqualTo(2);
        DonacionPendienteDTO primera = pagina.contenido().get(0);
        assertThat(primera.donacionId()).isEqualTo(conRanking.getId());
        assertThat(primera.donanteNombre()).isEqualTo("Ana Perez");
        assertThat(primera.ranking().estado()).isEqualTo(EstadoRanking.CALCULADO);
        assertThat(primera.ranking().sugerenciaPrincipal().entidadNombre()).isEqualTo("Comedor B");

        DonacionPendienteDTO segunda = pagina.contenido().get(1);
        assertThat(segunda.donacionId()).isEqualTo(sinRanking.getId());
        assertThat(segunda.ranking().estado()).isEqualTo(EstadoRanking.PENDIENTE);
        assertThat(segunda.ranking().sugerenciaPrincipal()).isNull();
    }

    @Test
    void rankingConCoincidenciasMuestraSoloEsaListaEnOrden() {
        Donacion donacion = donacion();
        coordinador.generarRecomendaciones();
        reiniciarContexto();

        RankingDonacionDTO ranking = service.obtenerRanking(donacion.getId()).orElseThrow();

        assertThat(ranking.ranking().tieneCoincidencias()).isTrue();
        assertThat(ranking.ranking().listas()).hasSize(1);
        RankingDonacionDTO.ListaRanking lista = ranking.ranking().listas().get(0);
        assertThat(lista.tipo()).isEqualTo(ResultadoAsignacion.LISTA_COINCIDENCIAS);
        assertThat(lista.entidades()).extracting(RankingDonacionDTO.Candidato::entidadNombre)
                .containsExactly("Comedor B", "Escuela C");
    }

    @Test
    void recalcularNoDuplicaElRankingDeLaDonacion() {
        Donacion donacion = donacion();
        coordinador.generarRecomendaciones();
        reiniciarContexto();
        coordinador.generarRecomendaciones();
        reiniciarContexto();

        assertThat(resultadosRepository.findAll()).hasSize(1);
        assertThat(resultadosRepository.findByDonacionId(donacion.getId())).isPresent();
    }

    @Test
    void elCalculoBorraElRankingDeDonacionesQueYaNoEstanEnDeposito() {
        Donacion donacion = donacion();
        coordinador.generarRecomendaciones();
        donacion.siguiente(b); // EN_DEPOSITO -> ASIGNACION_REALIZADA
        reiniciarContexto();

        coordinador.generarRecomendaciones();
        reiniciarContexto();

        assertThat(resultadosRepository.findByDonacionId(donacion.getId())).isEmpty();
        assertThat(service.obtenerRanking(donacion.getId())).isEmpty();
    }

    @Test
    void donacionesDeEntidadFiltraPorEstado() {
        Donacion asignada = donacion();
        asignada.siguiente(a);
        donacion(); // otra en depósito, sin entidad
        reiniciarContexto();

        PaginaDTO<DonacionDTO> todas = service.obtenerDonacionesDeEntidad(a.getId(), List.of(), 0, 20).orElseThrow();
        PaginaDTO<DonacionDTO> enTraslado = service
                .obtenerDonacionesDeEntidad(a.getId(), List.of(TipoEstado.EN_TRASLADO), 0, 20).orElseThrow();

        assertThat(todas.contenido()).extracting(DonacionDTO::getId).containsExactly(asignada.getId());
        assertThat(enTraslado.totalElementos()).isZero();
        assertThat(service.obtenerDonacionesDeEntidad(9999L, List.of(), 0, 20)).isEmpty();
    }

    private EntidadBeneficiaria entidad(String razonSocial) {
        EntidadBeneficiaria entidad = new EntidadBeneficiaria(TipoEntidadBeneficiaria.COMEDOR, "Calle 123",
                new Organizacion(razonSocial, "30-00000000-0"));
        em.persist(entidad);
        return entidad;
    }

    private Donacion donacion() {
        BienEstado bien = new BienEstado("Silla", "Silla de madera", subcategoria, true);
        em.persist(bien);
        Donacion donacion = new Donacion(donante, new ItemBien(bien, 6), subcategoria);
        em.persist(donacion);
        return donacion;
    }

    // Guarda lo pendiente y vacía el contexto, para que las consultas lean de la base
    // y devuelvan instancias nuevas (como pasa entre requests).
    private void reiniciarContexto() {
        em.flush();
        em.clear();
    }

    private static AlgoritmoAsignacion algoritmoFijo(String nombre, EntidadBeneficiaria... ranking) {
        List<Long> ids = java.util.Arrays.stream(ranking).map(EntidadBeneficiaria::getId).toList();
        return new AlgoritmoAsignacion() {
            @Override
            public List<EntidadBeneficiaria> generarRanking(Donacion donacion, List<EntidadBeneficiaria> entidades) {
                // Devuelve las instancias que recibe (las de la base), en el orden fijo.
                return ids.stream()
                        .map(id -> entidades.stream().filter(e -> id.equals(e.getId())).findFirst().orElseThrow())
                        .toList();
            }

            @Override
            public String nombre() {
                return nombre;
            }
        };
    }
}
