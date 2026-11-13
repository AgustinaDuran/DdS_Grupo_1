package org.donatrack.config;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Reglas sobre la identidad del token de Keycloak que llega desde el front.
 * Una cuenta solo puede consultar los datos asociados a su propio "sub"; ADMIN puede consultar cualquiera.
 */
public final class IdentidadAutenticada {

    private IdentidadAutenticada() {
    }

    public static boolean esAdmin(JwtAuthenticationToken autenticacion) {
        return autenticacion != null && autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> "ROLE_ADMIN".equals(autoridad.getAuthority()));
    }

    public static boolean esPropia(JwtAuthenticationToken autenticacion, String keycloakUserId) {
        return autenticacion != null && keycloakUserId != null
                && keycloakUserId.equals(autenticacion.getToken().getSubject());
    }

    public static boolean puedeConsultar(JwtAuthenticationToken autenticacion, String keycloakUserId) {
        return esPropia(autenticacion, keycloakUserId) || esAdmin(autenticacion);
    }

    /** Email del token solo si Keycloak lo marcó como verificado (cuenta local verificada o IdP confiable). */
    public static String emailVerificado(JwtAuthenticationToken autenticacion) {
        if (autenticacion == null) {
            return null;
        }
        Jwt token = autenticacion.getToken();
        return Boolean.TRUE.equals(token.getClaimAsBoolean("email_verified"))
                ? token.getClaimAsString("email")
                : null;
    }
}
