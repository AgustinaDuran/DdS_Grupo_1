# Autenticación de DonaTrack

Este componente usa Keycloak como proveedor de identidad OpenID Connect (SSO). La imagen importa el
realm `donatrack` al iniciar e incluye:

- cliente público `donatrack-frontend`: Authorization Code Flow con PKCE S256, usado por el servidor del
  front (SSR). El front guarda los tokens en su sesión de servidor, nunca en el navegador;
- cliente confidencial `donatrack-registration`: cuenta técnica (client credentials) con la que el front
  registra entidades y Donaciones crea las cuentas de los donantes;
- roles de realm `ADMIN`, `DONANTE` y `ENTIDAD`, emitidos en el claim `roles` del token;
- social login con Google y GitHub (opcional). Quien entra por primera vez con una red social recibe el
  rol `DONANTE`.

## Flujo

1. El front redirige a Keycloak (`/auth/start`; con `?provider=google|github` va directo a la red social).
2. Keycloak devuelve un `code` a `/auth/callback`; el front lo canjea (PKCE + state + nonce) por los tokens.
3. El front vincula la cuenta con Donaciones (`/api/entidades-beneficiarias/por-identidad/{sub}` o
   `/api/donantes/por-identidad/{sub}`). Un donante importado por CSV que entra con Google se vincula por
   su email, solo si Keycloak lo informa como verificado.
4. Cada llamada a las APIs lleva `Authorization: Bearer <access token>`. Los servicios validan la firma
   contra el JWKS de Keycloak y autorizan por rol. El front renueva el token con el refresh token antes
   de que venza (dura 5 minutos).
5. "Salir" cierra la sesión del front y la sesión SSO de Keycloak (RP-initiated logout).

## Levantar

Copiar `.env.example` a `.env` en la raíz de `DonaTrack`, completar los valores y:

```bash
docker compose up --build keycloak
```

Keycloak queda en `http://localhost:8084`; la consola administrativa usa `KEYCLOAK_ADMIN_USERNAME` y
`KEYCLOAK_ADMIN_PASSWORD`. Para probar ADMIN, crear el usuario desde la consola y asignarle el rol.

El realm no persiste entre `docker compose down`/`up` (igual que MySQL): se reimporta limpio.

## Social login

1. Crear las credenciales OAuth:
   - Google: Google Cloud Console → APIs y servicios → Credenciales → ID de cliente OAuth (aplicación web).
   - GitHub: Settings → Developer settings → OAuth Apps → New OAuth App.

   En ambos casos, la URL de redirección autorizada es
   `http://localhost:8084/realms/donatrack/broker/<google|github>/endpoint`.
2. En `DonaTrack/.env`: `GOOGLE_LOGIN_ENABLED=true`, `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET` (ídem
   `GITHUB_*`). Recrear Keycloak: `docker compose up -d --force-recreate keycloak`.
3. En el `.env` del front: `SOCIAL_LOGIN_PROVIDERS=google,github` (solo los habilitados).

GitHub no se marca como email confiable (`trustEmail=false`), así que esas cuentas no se vinculan por email
con donantes existentes; las de Google sí.
