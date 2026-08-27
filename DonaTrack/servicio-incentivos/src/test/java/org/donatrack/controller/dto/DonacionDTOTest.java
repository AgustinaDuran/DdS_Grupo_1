package org.donatrack.controller.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

/**
 * DonacionDTO es el único cuerpo que recibe el servicio y además viaja en la respuesta del
 * perfil analítico. Sus campos son package-private y sus getters usan PascalCase, así que sin
 * accessors JavaBean Jackson no detectaba ninguna propiedad: el JSON entrante llegaba con todo
 * en null (y reventaba al calcular la racha) y la respuesta no se podía serializar.
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

        assertThat(donacion.GetSubcategoria()).isEqualTo("alimentos");
        assertThat(donacion.GetBienes()).containsExactly("arroz", "fideos");
        assertThat(donacion.GetOrganizacion()).isEqualTo("Comedor Norte");
        assertThat(donacion.GetFechaIngreso()).isEqualTo(LocalDate.of(2026, 6, 5));
        assertThat(donacion.GetCantidadBienes()).isEqualTo(2);
    }

    @Test
    void seSerializaConSusPropiedades() throws Exception {
        DonacionDTO donacion = new DonacionDTO(
                "ana", "alimentos", List.of("arroz"), "Comedor Norte", LocalDate.of(2026, 6, 5));

        String json = mapper.writeValueAsString(donacion);

        assertThat(json).contains("\"subcategoria\":\"alimentos\"")
                .contains("\"organizacion\":\"Comedor Norte\"")
                .contains("arroz");
    }

    @Test
    void sinBienesLaCantidadEsCeroYNoExplota() throws Exception {
        DonacionDTO donacion = mapper.readValue(
                "{\"subcategoria\":\"alimentos\",\"fechaIngreso\":\"2026-06-05\"}", DonacionDTO.class);

        assertThat(donacion.GetCantidadBienes()).isZero();
    }
}
