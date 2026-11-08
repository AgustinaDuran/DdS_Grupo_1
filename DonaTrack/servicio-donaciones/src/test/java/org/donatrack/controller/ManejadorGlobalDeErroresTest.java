package org.donatrack.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.donatrack.dominio.algoritmo.EntidadFueraDelRankingException;
import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.service.DonacionesService;
import org.donatrack.service.excepciones.DonacionNoEncontradaException;
import org.donatrack.service.excepciones.RankingNoCalculadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// Cada excepción llega al cliente con su código HTTP y el cuerpo { status, codigo, mensaje }.
class ManejadorGlobalDeErroresTest {

    private DonacionesService donacionesService;
    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        donacionesService = mock(DonacionesService.class);
        mvc = MockMvcBuilders.standaloneSetup(new DonacionesController(donacionesService))
                .setControllerAdvice(new ManejadorGlobalDeErrores())
                .build();
    }

    @Test
    void donacionInexistenteDa404() throws Exception {
        falla(new DonacionNoEncontradaException("La donación no existe o fue eliminada."));

        asignar("{\"tipoEstado\":\"ASIGNACION_REALIZADA\",\"entidadId\":4}")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.codigo").value("DONACION_NO_ENCONTRADA"))
                .andExpect(jsonPath("$.mensaje").value("La donación no existe o fue eliminada."));
    }

    @Test
    void conflictoDeVersionDa409() throws Exception {
        falla(new ObjectOptimisticLockingFailureException(Donacion.class, 1L));

        asignar("{\"tipoEstado\":\"ASIGNACION_REALIZADA\",\"entidadId\":4}")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("CONFLICTO_CONCURRENCIA"))
                .andExpect(jsonPath("$.mensaje").value("Otra persona ya actualizó esta donación."));
    }

    @Test
    void sinRankingDa409() throws Exception {
        falla(new RankingNoCalculadoException("Esta donación todavía no tiene ranking."));

        asignar("{\"tipoEstado\":\"ASIGNACION_REALIZADA\",\"entidadId\":4}")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("RANKING_NO_CALCULADO"));
    }

    @Test
    void entidadFueraDelRankingDa422() throws Exception {
        falla(new EntidadFueraDelRankingException("La entidad elegida no está entre las sugeridas."));

        asignar("{\"tipoEstado\":\"ASIGNACION_REALIZADA\",\"entidadId\":99}")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.codigo").value("ENTIDAD_FUERA_DEL_RANKING"));
    }

    @Test
    void faltaUnDatoDa400() throws Exception {
        falla(new IllegalArgumentException("Falta elegir la entidad."));

        asignar("{\"tipoEstado\":\"ASIGNACION_REALIZADA\"}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("SOLICITUD_INVALIDA"))
                .andExpect(jsonPath("$.mensaje").value("Falta elegir la entidad."));
    }

    @Test
    void estadoInexistenteEnElJsonDa400ConElMismoFormato() throws Exception {
        asignar("{\"tipoEstado\":\"NO_EXISTE\"}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.codigo").value("BAD_REQUEST"));
    }

    @Test
    void errorInesperadoDa500SinMostrarDetallesInternos() throws Exception {
        falla(new RuntimeException("NullPointer en la línea 42"));

        asignar("{\"tipoEstado\":\"VENCIDA\"}")
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.codigo").value("ERROR_INTERNO"))
                .andExpect(jsonPath("$.mensaje").value("Ocurrió un error inesperado. Intentá de nuevo más tarde."));
    }

    private void falla(RuntimeException e) {
        when(donacionesService.actualizarDonacion(anyLong(), any())).thenThrow(e);
    }

    private ResultActions asignar(String json) throws Exception {
        return mvc.perform(patch("/api/donaciones/1").contentType(MediaType.APPLICATION_JSON).content(json));
    }
}
