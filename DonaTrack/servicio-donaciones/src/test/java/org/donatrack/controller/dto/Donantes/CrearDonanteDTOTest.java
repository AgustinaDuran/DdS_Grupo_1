package org.donatrack.controller.dto.Donantes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * El contacto del donante se recibe como "contactoPredeterminado"; el nombre viejo
 * "contactoDTOPredeterminado" se sigue aceptando por compatibilidad.
 */
class CrearDonanteDTOTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void aceptaContactoPredeterminado() throws Exception {
        CrearDonanteDTO dto = mapper.readValue(
                "{\"tipo\":\"HUMANA\",\"contactoPredeterminado\":{\"medio\":\"MAIL\",\"valor\":\"ana@mail.com\"}}",
                CrearDonanteDTO.class);

        assertNotNull(dto.getContactoDTOPredeterminado());
        assertEquals("ana@mail.com", dto.getContactoDTOPredeterminado().getValor());
    }

    @Test
    void sigueAceptandoElNombreViejo() throws Exception {
        CrearDonanteDTO dto = mapper.readValue(
                "{\"tipo\":\"HUMANA\",\"contactoDTOPredeterminado\":{\"medio\":\"MAIL\",\"valor\":\"ana@mail.com\"}}",
                CrearDonanteDTO.class);

        assertNotNull(dto.getContactoDTOPredeterminado());
        assertEquals("MAIL", dto.getContactoDTOPredeterminado().getMedio());
    }
}
