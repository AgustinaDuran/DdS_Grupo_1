package org.donatrack.controller.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.donatrack.model.DonacionRegistrada;
import org.junit.jupiter.api.Test;

/**
 * DonacionDTO es el único cuerpo que recibe el servicio y además viaja en la respuesta del
 * perfil analítico. Es un DTO puro: Jackson lo (de)serializa por sus accessors JavaBean y el
 * dominio trabaja con {@link DonacionRegistrada}, que es lo que se persiste.
 */
class DonacionDTOTest {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final String JSON = """
            {
              "subcategoria": "alimentos",
              "bienes": ["arroz", "fideos"],
              "organizacion": "Comedor Norte",
              "fechaIngreso": "2026-06-05"
            }
            """;

    @Test
    void deserializaElCuerpoQueLlegaPorRest() throws Exception {
        DonacionDTO donacion = mapper.readValue(JSON, DonacionDTO.class);

        assertThat(donacion.getSubcategoria()).isEqualTo("alimentos");
        assertThat(donacion.getBienes()).containsExactly("arroz", "fideos");
        assertThat(donacion.getOrganizacion()).isEqualTo("Comedor Norte");
        assertThat(donacion.getFechaIngreso()).isEqualTo(LocalDate.of(2026, 6, 5));
    }

    @Test
    void seSerializaConSusPropiedades() throws Exception {
        DonacionDTO donacion = DonacionDTO.desde(new DonacionRegistrada(
                "ana", "alimentos", List.of("arroz"), "Comedor Norte", LocalDate.of(2026, 6, 5)));

        String json = mapper.writeValueAsString(donacion);

        assertThat(json).contains("\"nombreUsuario\":\"ana\"")
                .contains("\"subcategoria\":\"alimentos\"")
                .contains("\"organizacion\":\"Comedor Norte\"")
                .contains("arroz");
    }

    @Test
    void seConvierteAlDominio() throws Exception {
        DonacionDTO dto = mapper.readValue(JSON, DonacionDTO.class);
        dto.setNombreUsuario("ana");

        DonacionRegistrada dominio = dto.toDominio();

        assertThat(dominio.GetId()).isNull();
        assertThat(dominio.GetNombreUsuario()).isEqualTo("ana");
        assertThat(dominio.GetSubcategoria()).isEqualTo("alimentos");
        assertThat(dominio.GetBienes()).containsExactly("arroz", "fideos");
        assertThat(dominio.GetOrganizacion()).isEqualTo("Comedor Norte");
        assertThat(dominio.GetFechaIngreso()).isEqualTo(LocalDate.of(2026, 6, 5));
        assertThat(dominio.GetCantidadBienes()).isEqualTo(2);
    }

    @Test
    void sinBienesLaCantidadEsCeroYNoExplota() throws Exception {
        DonacionDTO donacion = mapper.readValue(
                "{\"subcategoria\":\"alimentos\",\"fechaIngreso\":\"2026-06-05\"}", DonacionDTO.class);

        assertThat(donacion.toDominio().GetCantidadBienes()).isZero();
    }
}
