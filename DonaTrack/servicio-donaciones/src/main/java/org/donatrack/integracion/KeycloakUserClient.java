package org.donatrack.integracion;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.HttpClientErrorException;

/** Crea cuentas de donantes en Keycloak mediante la cuenta técnica del sistema. */
@Component
public class KeycloakUserClient {
    private final RestClient keycloak;
    private final String realm;
    private final String clientId;
    private final String clientSecret;

    public KeycloakUserClient(RestClient.Builder restClientBuilder,
                              @Value("${services.keycloak.url:http://localhost:8084}") String keycloakUrl,
                              @Value("${services.keycloak.realm:donatrack}") String realm,
                              @Value("${services.keycloak.registration-client-id:donatrack-registration}") String clientId,
                              @Value("${services.keycloak.registration-client-secret:}") String clientSecret) {
        this.keycloak = restClientBuilder.baseUrl(keycloakUrl).build();
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /** La contraseña temporal se reemplaza obligatoriamente en el primer inicio de sesión. */
    public String crearDonante(String email, String nombre, String apellido, String contrasenaTemporal) {
        if (clientSecret.isBlank()) {
            throw new IllegalStateException("No está configurada la credencial técnica para crear cuentas.");
        }
        String token = obtenerTokenTecnico();
        Map<String, Object> usuario = Map.of(
                "username", email, "email", email, "enabled", true,
                "firstName", nombre == null ? "" : nombre,
                "lastName", apellido == null ? "" : apellido,
                "requiredActions", List.of("UPDATE_PASSWORD"),
                "credentials", List.of(Map.of("type", "password", "value", contrasenaTemporal, "temporary", true)));
        try {
            URI ubicacion = keycloak.post().uri("/admin/realms/{realm}/users", realm)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON).body(usuario).retrieve()
                    .toBodilessEntity().getHeaders().getLocation();
            if (ubicacion == null) {
                throw new IllegalStateException("Keycloak no devolvió el identificador de la cuenta creada.");
            }
            String id = ubicacion.getPath().substring(ubicacion.getPath().lastIndexOf('/') + 1);
            asignarRolDonante(token, id);
            return id;
        } catch (HttpClientErrorException.Conflict exception) {
            throw new IllegalArgumentException("Ya existe una cuenta de acceso con ese correo.");
        } catch (RestClientResponseException exception) {
            throw new IllegalStateException("No se pudo crear la cuenta de acceso del donante.");
        }
    }

    public void eliminarCuenta(String keycloakUserId) {
        if (keycloakUserId == null || keycloakUserId.isBlank() || clientSecret.isBlank()) return;
        try {
            keycloak.delete().uri("/admin/realms/{realm}/users/{id}", realm, keycloakUserId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + obtenerTokenTecnico())
                    .retrieve().toBodilessEntity();
        } catch (RestClientResponseException ignored) {
            // No se oculta el error original si la compensación remota falla.
        }
    }

    private String obtenerTokenTecnico() {
        MultiValueMap<String, String> formulario = new LinkedMultiValueMap<>();
        formulario.add("grant_type", "client_credentials");
        formulario.add("client_id", clientId);
        formulario.add("client_secret", clientSecret);
        Map<?, ?> respuesta = keycloak.post().uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(formulario).retrieve().body(Map.class);
        Object token = respuesta == null ? null : respuesta.get("access_token");
        if (!(token instanceof String accessToken) || accessToken.isBlank()) {
            throw new IllegalStateException("Keycloak no devolvió un token técnico.");
        }
        return accessToken;
    }

    @SuppressWarnings("unchecked")
    private void asignarRolDonante(String token, String keycloakUserId) {
        Map<String, Object> rol = keycloak.get().uri("/admin/realms/{realm}/roles/DONANTE", realm)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token).retrieve().body(Map.class);
        keycloak.post().uri("/admin/realms/{realm}/users/{id}/role-mappings/realm", realm, keycloakUserId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                .body(List.of(rol)).retrieve().toBodilessEntity();
    }
}
