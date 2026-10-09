package org.donatrack.controller.dto.Donantes;

import java.util.ArrayList;
import java.util.List;

/**
 * Resumen del resultado de una importación masiva de personas donantes por CSV.
 * Reporta cuántos registros se crearon, cuántos se actualizaron (upsert por email) y
 * el detalle de las filas que no se pudieron procesar.
 */
public class ImportacionCsvResultadoDTO {

    private int totalProcesados;
    private int creados;
    private int actualizados;
    private final List<ErrorImportacion> errores = new ArrayList<>();

    public int getTotalProcesados() {
        return totalProcesados;
    }

    public int getCreados() {
        return creados;
    }

    public int getActualizados() {
        return actualizados;
    }

    public List<ErrorImportacion> getErrores() {
        return errores;
    }

    public void registrarCreado() {
        this.creados++;
        this.totalProcesados++;
    }

    public void registrarActualizado() {
        this.actualizados++;
        this.totalProcesados++;
    }

    public void registrarError(int linea, String motivo) {
        this.errores.add(new ErrorImportacion(linea, motivo));
        this.totalProcesados++;
    }

    public static class ErrorImportacion {
        private final int linea;
        private final String motivo;

        public ErrorImportacion(int linea, String motivo) {
            this.linea = linea;
            this.motivo = motivo;
        }

        public int getLinea() {
            return linea;
        }

        public String getMotivo() {
            return motivo;
        }
    }
}
