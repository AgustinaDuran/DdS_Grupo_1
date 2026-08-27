package org.donatrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.controller.dto.Donantes.CrearDonanteDTO;
import org.donatrack.controller.dto.Donantes.ImportacionCsvResultadoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class ImportacionDonantesServiceTest {

    @Test
    void importaCreaActualizaYReportaFilasInvalidas() {
        DonantesService donantesService = mock(DonantesService.class);
        Donante existente = mock(Donante.class);
        Organizacion datosExistente = new Organizacion("Nombre anterior", "30-anterior");
        when(existente.getDatosUsuario()).thenReturn(datosExistente);

        Map<String, Donante> indice = new HashMap<>();
        indice.put("ana@mail.com", existente);
        when(donantesService.indexarPorEmail()).thenReturn(indice);

        Donante creado = mock(Donante.class);
        Organizacion datosCreado = new Organizacion("Organizacion nueva", "30-nueva");
        when(creado.getDatosUsuario()).thenReturn(datosCreado);
        when(donantesService.registrarDonante(any(CrearDonanteDTO.class))).thenReturn(creado);

        String csv = "TipoPersona,TipoDoc,Documento,Nombre/Razon Social,Email,Telefono\n"
                + "HUMANA,DNI,123,Ana Actualizada,ANA@MAIL.COM,111\n"
                + "JURIDICA,CUIT,30-123,Arcos Plateados,empresa@mail.com,222\n"
                + "OTRO,DNI,999,Registro Invalido,invalido@mail.com,333\n";

        ImportacionCsvResultadoDTO resultado = new ImportacionDonantesService(donantesService)
                .generarDonantesDesdeCSV(new MockMultipartFile("file", "donantes.csv",
                        "text/csv", csv.getBytes(java.nio.charset.StandardCharsets.UTF_8)));

        assertThat(resultado.getTotalProcesados()).isEqualTo(3);
        assertThat(resultado.getActualizados()).isEqualTo(1);
        assertThat(resultado.getCreados()).isEqualTo(1);
        assertThat(resultado.getErrores()).hasSize(1);
        assertThat(resultado.getErrores().get(0).getLinea()).isEqualTo(4);
        assertThat(datosExistente.getContactos()).extracting(Contacto::getValor).containsExactly("111");
        assertThat(datosCreado.getContactos()).extracting(Contacto::getValor).containsExactly("222");
        verify(donantesService, times(1)).registrarDonante(any(CrearDonanteDTO.class));
    }
}