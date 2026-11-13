# Autenticación de DonaTrack

Este componente usa Keycloak como proveedor de identidad OpenID Connect. La
imagen importa el realm `donatrack` al iniciar por primera vez e incluye:

- cliente público `donatrack-frontend`;
- Authorization Code Flow con PKCE S256;
- roles de realm `ADMIN`, `DONANTE` y `ENTIDAD`.

Las claves y contraseñas no se versionan. Copiar `.env.example` a `.env` en la
raíz de `DonaTrack`, completar todos los valores y levantar con:

```bash
docker compose up --build keycloak
```

Keycloak queda disponible en `http://localhost:8084`; la consola administrativa
usa las credenciales `KEYCLOAK_ADMIN_USERNAME` y
`KEYCLOAK_ADMIN_PASSWORD`. Los usuarios de la aplicación se crean desde esa
consola y se les asigna uno de los tres roles.
