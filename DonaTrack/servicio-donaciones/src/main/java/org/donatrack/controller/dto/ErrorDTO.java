package org.donatrack.controller.dto;

// Cuerpo uniforme de todas las respuestas de error del servicio.
// status: código HTTP. codigo: identificador estable para que el front decida qué hacer.
// mensaje: texto listo para mostrar al usuario (por ejemplo, en un toast).
public record ErrorDTO(int status, String codigo, String mensaje) {
}
