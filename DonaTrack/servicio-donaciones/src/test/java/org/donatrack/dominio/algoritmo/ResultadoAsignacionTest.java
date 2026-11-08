package org.donatrack.dominio.algoritmo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.donatrack.dominio.donacion.Donacion;
import org.donatrack.dominio.donacion.TipoEstado;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.entidadBeneficiaria.TipoEntidadBeneficiaria;
import org.junit.jupiter.api.Test;

// Regla de negocio de la asignación: solo se puede elegir una entidad del ranking.
class ResultadoAsignacionTest {

    private final EntidadBeneficiaria a = entidad(1L);
    private final EntidadBeneficiaria b = entidad(2L);
    private final EntidadBeneficiaria c = entidad(3L);

    @Test
    void conCoincidenciasAsignaUnaEntidadCoincidente() {
        Donacion donacion = donacionEnDeposito();
        ResultadoAsignacion resultado = new ResultadoAsignacion(donacion, rankings(List.of(a, b), List.of(b, c)), List.of(b));

        resultado.confirmarDestinoFinal(b);

        assertThat(donacion.getEstadoDonacion().getEstado()).isEqualTo(TipoEstado.ASIGNACION_REALIZADA);
        assertThat(donacion.getEntidadAEntregar()).isEqualTo(b);
    }

    @Test
    void conCoincidenciasNoPermiteElegirUnaEntidadQueSoloEstaEnUnAlgoritmo() {
        Donacion donacion = donacionEnDeposito();
        ResultadoAsignacion resultado = new ResultadoAsignacion(donacion, rankings(List.of(a, b), List.of(b, c)), List.of(b));

        assertThatThrownBy(() -> resultado.confirmarDestinoFinal(a))
                .isInstanceOf(EntidadFueraDelRankingException.class);
        assertThat(donacion.getEstadoDonacion().getEstado()).isEqualTo(TipoEstado.EN_DEPOSITO);
    }

    @Test
    void sinCoincidenciasPermiteElegirDeCualquierAlgoritmo() {
        Donacion donacion = donacionEnDeposito();
        ResultadoAsignacion resultado = new ResultadoAsignacion(donacion, rankings(List.of(a), List.of(c)), List.of());

        resultado.confirmarDestinoFinal(c);

        assertThat(resultado.tieneCoincidencias()).isFalse();
        assertThat(donacion.getEntidadAEntregar()).isEqualTo(c);
    }

    @Test
    void entidadQueNoEstaEnNingunaListaEsRechazada() {
        ResultadoAsignacion resultado = new ResultadoAsignacion(donacionEnDeposito(), rankings(List.of(a), List.of(b)), List.of());

        assertThatThrownBy(() -> resultado.confirmarDestinoFinal(c))
                .isInstanceOf(EntidadFueraDelRankingException.class);
    }

    @Test
    void conListasNulasNoHayNullPointerException() {
        ResultadoAsignacion resultado = new ResultadoAsignacion(donacionEnDeposito(), null, null);

        assertThat(resultado.tieneCoincidencias()).isFalse();
        assertThat(resultado.getCoincidencias()).isEmpty();
        assertThat(resultado.getRankingsPorAlgoritmo()).isEmpty();
        assertThatThrownBy(() -> resultado.confirmarDestinoFinal(a))
                .isInstanceOf(EntidadFueraDelRankingException.class);
    }

    @Test
    void recalcularReemplazaElRankingAnterior() {
        ResultadoAsignacion resultado = new ResultadoAsignacion(donacionEnDeposito(), rankings(List.of(a), List.of(a)), List.of(a));

        resultado.recalcular(rankings(List.of(b), List.of(c)), List.of());

        assertThat(resultado.tieneCoincidencias()).isFalse();
        assertThat(resultado.getRankingsPorAlgoritmo().get("ALGO_1")).containsExactly(b);
        assertThat(resultado.getRankingsPorAlgoritmo().get("ALGO_2")).containsExactly(c);
    }

    private static Map<String, List<EntidadBeneficiaria>> rankings(List<EntidadBeneficiaria> algo1,
                                                                   List<EntidadBeneficiaria> algo2) {
        Map<String, List<EntidadBeneficiaria>> rankings = new LinkedHashMap<>();
        rankings.put("ALGO_1", algo1);
        rankings.put("ALGO_2", algo2);
        return rankings;
    }

    private static Donacion donacionEnDeposito() {
        return new Donacion(null, null, null);
    }

    private static EntidadBeneficiaria entidad(Long id) {
        EntidadBeneficiaria entidad = new EntidadBeneficiaria(TipoEntidadBeneficiaria.COMEDOR, "Calle " + id, null);
        entidad.setId(id);
        return entidad;
    }
}
