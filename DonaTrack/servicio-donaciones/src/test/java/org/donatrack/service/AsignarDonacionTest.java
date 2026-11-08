package org.donatrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.donatrack.controller.dto.Donaciones.ActualizarDonacionDTO;
import org.donatrack.dominio.algoritmo.EntidadFueraDelRankingException;
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
import org.donatrack.integracion.DestinatarioResolver;
import org.donatrack.integracion.IncentivosClient;
import org.donatrack.integracion.NotificacionesClient;
import org.donatrack.integracion.broker.BrokerLogistica;
import org.donatrack.repository.DonacionesCompletasRepository;
import org.donatrack.repository.DonacionesRepository;
import org.donatrack.repository.EntidadesBeneficiariasRepository;
import org.donatrack.repository.ResultadoAsignacionRepository;
import org.donatrack.service.excepciones.DonacionNoEncontradaException;
import org.donatrack.service.excepciones.RankingNoCalculadoException;
import org.donatrack.service.excepciones.TransicionEstadoInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import jakarta.persistence.EntityManager;

// PATCH /api/donaciones/{id} con ASIGNACION_REALIZADA y VENCIDA (fase 5), contra una base H2.
// Las integraciones (notificaciones, logística, incentivos) son mocks.
@DataJpaTest
class AsignarDonacionTest {

    @Autowired private EntityManager em;
    @Autowired private DonacionesRepository donacionesRepository;
    @Autowired private DonacionesCompletasRepository donacionesCompletasRepository;
    @Autowired private EntidadesBeneficiariasRepository entidadesRepository;
    @Autowired private ResultadoAsignacionRepository resultadosRepository;

    private BrokerLogistica brokerLogistica;
    private DonacionesService service;

    private EntidadBeneficiaria sugerida;
    private EntidadBeneficiaria noSugerida;
    private Donacion donacion;

    @BeforeEach
    void preparar() {
        brokerLogistica = mock(BrokerLogistica.class);
        service = new DonacionesService(donacionesRepository, donacionesCompletasRepository,
                mock(DonantesService.class), new EntidadesBeneficiariasService(entidadesRepository),
                mock(BienesService.class), mock(NotificacionesClient.class), mock(IncentivosClient.class),
                brokerLogistica, mock(DestinatarioResolver.class), resultadosRepository);

        Categoria categoria = new Categoria("Mobiliario");
        Subcategoria subcategoria = new Subcategoria("Sillas", categoria);
        Persona persona = new Persona("Ana", "Perez", 30, "12345678", "F", "Calle 1",
                new Contacto("MAIL", "ana@ejemplo.com"));
        persona.agregarContacto(persona.getContactoPredeterminado());
        DonantePersona donante = new DonantePersona(persona);
        BienEstado bien = new BienEstado("Silla", "Silla de madera", subcategoria, true);
        em.persist(categoria);
        em.persist(subcategoria);
        em.persist(persona);
        em.persist(donante);
        em.persist(bien);

        sugerida = entidad("Hogar sugerido");
        noSugerida = entidad("Comedor no sugerido");

        donacion = new Donacion(donante, new ItemBien(bien, 6), subcategoria);
        em.persist(donacion);
        reiniciarContexto();
    }

    @Test
    void asignarAUnaEntidadDelRankingCambiaElEstadoYBorraElRanking() {
        guardarRankingCon(sugerida);

        service.actualizarDonacion(donacion.getId(), asignarA(sugerida.getId()));
        reiniciarContexto();

        Donacion guardada = donacionesRepository.findById(donacion.getId()).orElseThrow();
        assertThat(guardada.getEstadoDonacion().getEstado()).isEqualTo(TipoEstado.ASIGNACION_REALIZADA);
        assertThat(guardada.getEntidadAEntregar().getId()).isEqualTo(sugerida.getId());
        assertThat(resultadosRepository.findByDonacionId(donacion.getId())).isEmpty();
        verify(brokerLogistica).registrarEntregas(any());
    }

    @Test
    void asignarAUnaEntidadFueraDelRankingFallaYNoAvisaALogistica() {
        guardarRankingCon(sugerida);

        assertThatThrownBy(() -> service.actualizarDonacion(donacion.getId(), asignarA(noSugerida.getId())))
                .isInstanceOf(EntidadFueraDelRankingException.class);
        verify(brokerLogistica, never()).registrarEntregas(any());
    }

    @Test
    void asignarSinRankingCalculadoFalla() {
        assertThatThrownBy(() -> service.actualizarDonacion(donacion.getId(), asignarA(sugerida.getId())))
                .isInstanceOf(RankingNoCalculadoException.class);
    }

    @Test
    void asignarSinEntidadFalla() {
        guardarRankingCon(sugerida);

        assertThatThrownBy(() -> service.actualizarDonacion(donacion.getId(), asignarA(null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Falta elegir la entidad.");
    }

    @Test
    void asignarDosVecesLaMismaDonacionFallaLaSegunda() {
        guardarRankingCon(sugerida);
        service.actualizarDonacion(donacion.getId(), asignarA(sugerida.getId()));
        reiniciarContexto();

        assertThatThrownBy(() -> service.actualizarDonacion(donacion.getId(), asignarA(sugerida.getId())))
                .isInstanceOf(TransicionEstadoInvalidaException.class);
    }

    @Test
    void donacionInexistenteFalla() {
        assertThatThrownBy(() -> service.actualizarDonacion(999_999L, asignarA(sugerida.getId())))
                .isInstanceOf(DonacionNoEncontradaException.class);
    }

    @Test
    void marcarComoVencidaBorraElRanking() {
        guardarRankingCon(sugerida);

        ActualizarDonacionDTO vencer = new ActualizarDonacionDTO();
        vencer.setTipoEstado(TipoEstado.VENCIDA);
        service.actualizarDonacion(donacion.getId(), vencer);
        reiniciarContexto();

        assertThat(donacionesRepository.findById(donacion.getId()).orElseThrow().getEstadoDonacion().getEstado())
                .isEqualTo(TipoEstado.VENCIDA);
        assertThat(resultadosRepository.findByDonacionId(donacion.getId())).isEmpty();
    }

    // Simula dos administradores: los dos leen la donación en la versión 0; el primero guarda
    // (pasa a la versión 1) y el segundo intenta guardar su copia vieja.
    @Test
    void guardarUnaCopiaDesactualizadaFallaPorVersion() {
        Donacion copiaVieja = donacionesRepository.findById(donacion.getId()).orElseThrow();
        em.detach(copiaVieja);

        Donacion copiaActual = donacionesRepository.findById(donacion.getId()).orElseThrow();
        copiaActual.setFotoEntrega("primera.jpg");
        donacionesRepository.saveAndFlush(copiaActual);

        copiaVieja.setFotoEntrega("segunda.jpg");
        assertThatThrownBy(() -> donacionesRepository.saveAndFlush(copiaVieja))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    private void guardarRankingCon(EntidadBeneficiaria entidad) {
        Donacion enContexto = donacionesRepository.findById(donacion.getId()).orElseThrow();
        EntidadBeneficiaria entidadEnContexto = entidadesRepository.findById(entidad.getId()).orElseThrow();
        Map<String, List<EntidadBeneficiaria>> rankings = new LinkedHashMap<>();
        rankings.put("ALGO_1", List.of(entidadEnContexto));
        rankings.put("ALGO_2", List.of(entidadEnContexto));
        resultadosRepository.save(new ResultadoAsignacion(enContexto, rankings, List.of(entidadEnContexto)));
        reiniciarContexto();
    }

    private static ActualizarDonacionDTO asignarA(Long entidadId) {
        ActualizarDonacionDTO dto = new ActualizarDonacionDTO();
        dto.setTipoEstado(TipoEstado.ASIGNACION_REALIZADA);
        dto.setEntidadId(entidadId);
        return dto;
    }

    private EntidadBeneficiaria entidad(String razonSocial) {
        EntidadBeneficiaria entidad = new EntidadBeneficiaria(TipoEntidadBeneficiaria.COMEDOR, "Calle 123",
                new Organizacion(razonSocial, "30-00000000-0"));
        em.persist(entidad);
        return entidad;
    }

    private void reiniciarContexto() {
        em.flush();
        em.clear();
    }
}
