package org.donatrack.controller.dto;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

// Página genérica para las respuestas paginadas. Se usa en lugar de serializar PageImpl,
// porque Spring advierte que su JSON no es estable entre versiones.
public record PaginaDTO<T>(
        List<T> contenido,
        int pagina,
        int tamanio,
        long totalElementos,
        int totalPaginas) {

    public static <E, T> PaginaDTO<T> de(Page<E> pagina, Function<E, T> aDTO) {
        return new PaginaDTO<>(
                pagina.getContent().stream().map(aDTO).toList(),
                pagina.getNumber(),
                pagina.getSize(),
                pagina.getTotalElements(),
                pagina.getTotalPages());
    }
}
