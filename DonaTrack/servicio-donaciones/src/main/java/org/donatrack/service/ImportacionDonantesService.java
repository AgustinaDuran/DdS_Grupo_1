package org.donatrack.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.donatrack.controller.dto.Donantes.ContactoDTO;
import org.donatrack.controller.dto.Donantes.CrearDonanteDTO;
import org.donatrack.controller.dto.Donantes.DonanteImportadoDTO;
import org.donatrack.controller.dto.Donantes.ImportacionCsvResultadoDTO;
import org.donatrack.controller.dto.Donantes.TipoDonante;
import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.donatrack.dominio.usuario.RolUsuario;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.dominio.usuario.persona.Persona;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Importación masiva de personas donantes desde un archivo CSV (Entrega 1).
 *
 * <p>Cada línea representa una persona donante (HUMANA o JURIDICA). El procesamiento es
 * sincrónico y en streaming: se lee el archivo línea por línea y se resuelve un upsert por
 * email (si el email ya existe se actualiza, si no se crea). Para que la resolución sea
 * eficiente sobre archivos grandes (10.000+ filas) se construye una única vez un índice
 * email -> donante y se lo mantiene actualizado a medida que se crean nuevos registros.</p>
 */
@Service
public class ImportacionDonantesService {

    private static final String MEDIO_EMAIL = "MAIL";
    private static final String MEDIO_TELEFONO = "SMS";
    private static final int COLUMNAS_ESPERADAS = 6;

    private final DonantesService donantesService;

    public ImportacionDonantesService(DonantesService donantesService) {
        this.donantesService = donantesService;
    }

    public ImportacionCsvResultadoDTO generarDonantesDesdeCSV(MultipartFile archivo) {
        ImportacionCsvResultadoDTO resultado = new ImportacionCsvResultadoDTO();

        if (archivo == null || archivo.isEmpty()) {
            resultado.registrarError(0, "El archivo está vacío o no fue provisto.");
            return resultado;
        }

        Map<String, Donante> indice = donantesService.indexarPorEmail();

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

            String encabezado = lector.readLine();
            if (encabezado == null) {
                resultado.registrarError(0, "El archivo no contiene encabezado ni datos.");
                return resultado;
            }
            char separador = detectarSeparador(encabezado);

            String linea;
            int numeroLinea = 1; // el encabezado es la línea 1
            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                if (linea.isBlank()) {
                    continue;
                }
                procesarLinea(linea, separador, numeroLinea, indice, resultado);
            }
        } catch (IOException e) {
            resultado.registrarError(0, "No se pudo leer el archivo: " + e.getMessage());
        }

        return resultado;
    }

    private void procesarLinea(String linea, char separador, int numeroLinea,
                               Map<String, Donante> indice, ImportacionCsvResultadoDTO resultado) {
        try {
            DonanteImportadoDTO fila = parsear(linea, separador);
            String email = fila.getEmail().trim().toLowerCase();

            Donante existente = indice.get(email);
            if (existente != null) {
                actualizarDonante(existente, fila);
                resultado.registrarActualizado();
            } else {
                Donante creado = generarDonante(fila);   // extraído a método propio
                indice.put(email, creado);
                resultado.registrarCreado();
            }
        } catch (Exception e) {
            resultado.registrarError(numeroLinea, e.getMessage());
        }
    }

    private Donante generarDonante(DonanteImportadoDTO fila) {
        Donante creado = donantesService.registrarDonante(mapearACrearDonanteDTO(fila));
        agregarTelefono(creado.getDatosUsuario(), fila.getTelefono());
        return creado;
    }

    private DonanteImportadoDTO parsear(String linea, char separador) {
        String[] campos = linea.split(java.util.regex.Pattern.quote(String.valueOf(separador)), -1);
        if (campos.length < COLUMNAS_ESPERADAS) {
            throw new IllegalArgumentException("Se esperaban " + COLUMNAS_ESPERADAS
                    + " columnas y se encontraron " + campos.length + ".");
        }

        String tipoPersonaRaw = campos[0].trim();
        String email = campos[4].trim();

        if (email.isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }

        TipoDonante tipoPersona;
        try {
            tipoPersona = TipoDonante.valueOf(tipoPersonaRaw.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("TipoPersona inválido: '" + tipoPersonaRaw
                    + "'. Valores admitidos: HUMANA, JURIDICA.");
        }

        String nombreRazonSocial = campos[3].trim();
        if (nombreRazonSocial.isEmpty()) {
            throw new IllegalArgumentException("El nombre / razón social es obligatorio.");
        }

        return new DonanteImportadoDTO(
                tipoPersona,
                campos[1].trim(),
                campos[2].trim(),
                nombreRazonSocial,
                email,
                campos[5].trim());
    }

    private CrearDonanteDTO mapearACrearDonanteDTO(DonanteImportadoDTO fila) {
        CrearDonanteDTO dto = new CrearDonanteDTO();
        dto.setTipo(fila.getTipoPersona());
        dto.setContactoDTOPredeterminado(new ContactoDTO(MEDIO_EMAIL, fila.getEmail()));

        if (fila.getTipoPersona() == TipoDonante.HUMANA) {
            dto.setTipoUsuario(RolUsuario.PERSONA);
            String[] nombreApellido = separarNombreApellido(fila.getNombreRazonSocial());
            dto.setNombre(nombreApellido[0]);
            dto.setApellido(nombreApellido[1]);
            dto.setDni(fila.getDocumento());
        } else {
            dto.setTipoUsuario(RolUsuario.ORGANIZACION);
            dto.setRazonSocial(fila.getNombreRazonSocial());
            dto.setCuit(fila.getDocumento());
        }
        return dto;
    }

    /**
     * Actualiza en el lugar los datos del donante existente. Los repositorios son en memoria y
     * conservan la referencia del objeto, por lo que basta con mutarlo (invocar save lo
     * duplicaría en la lista). El email, clave del upsert, no se modifica.
     */
    private void actualizarDonante(Donante existente, DonanteImportadoDTO fila) {
        DatosUsuario datos = existente.getDatosUsuario();
        if (datos instanceof Persona persona) {
            String[] nombreApellido = separarNombreApellido(fila.getNombreRazonSocial());
            persona.setNombre(nombreApellido[0]);
            persona.setApellido(nombreApellido[1]);
            persona.setDni(fila.getDocumento());
        } else if (datos instanceof Organizacion organizacion) {
            organizacion.setRazonSocial(fila.getNombreRazonSocial());
            organizacion.setCuit(fila.getDocumento());
        }
        agregarTelefono(datos, fila.getTelefono());
    }

    /** Reemplaza (o agrega) el contacto telefónico del usuario si la fila trae teléfono. */
    private void agregarTelefono(DatosUsuario datos, String telefono) {
        if (datos == null || telefono == null || telefono.isBlank()) {
            return;
        }
        datos.getContactos().removeIf(c -> MEDIO_TELEFONO.equalsIgnoreCase(c.getMedio()));
        datos.agregarContacto(new Contacto(MEDIO_TELEFONO, telefono.trim()));
    }

    private String[] separarNombreApellido(String nombreCompleto) {
        String limpio = nombreCompleto.trim();
        int corte = limpio.indexOf(' ');
        if (corte < 0) {
            return new String[]{limpio, ""};
        }
        return new String[]{limpio.substring(0, corte), limpio.substring(corte + 1).trim()};
    }

    private char detectarSeparador(String encabezado) {
        long puntoYComa = encabezado.chars().filter(c -> c == ';').count();
        long comas = encabezado.chars().filter(c -> c == ',').count();
        return puntoYComa > comas ? ';' : ',';
    }
}
