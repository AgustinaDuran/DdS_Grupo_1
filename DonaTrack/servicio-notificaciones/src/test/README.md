# Tests del Servicio de Notificaciones

Cubren los mismos casos que se probaron a mano con `curl`, pero sin levantar el servicio
ni necesitar credenciales de Brevo.

## Qué se prueba

| Archivo | Qué verifica |
|---|---|
| `model/MediosDeEnvioTest` | `EnvioMail` delega en el cliente de email; `EnvioSMS` y `EnvioWhatsapp` fallan en vez de simular éxito |
| `model/NotificacionTest` | Estados (`PENDIENTE` → `ENVIADA` / `FALLIDA`), motivo del fallo, ids únicos |
| `repository/NotificacionesRepositoryTest` | Guardar, listar, borrar. Incluye las dos regresiones: `findById` con ids > 127 y guardar después de borrar |
| `service/MedioEnvioFactoryTest` | Resolución del tipo de contacto (`MAIL`/`mail`/`EMAIL`), tipos no soportados y tipo nulo |
| `service/NotificadorTest` | El núcleo: envío exitoso, salto de medios no soportados, prioridad de contactos, fallo del proveedor, historial |
| `controller/NotificadorControllerTest` | El contrato HTTP que consume `servicio-donaciones`: 200 con el estado real, 400 ante pedidos inválidos |
| `integracion/EnvioRealDeMailIT` | **Envío real contra Brevo.** Apagado por defecto (ver abajo) |

## Cómo correrlos

### Desde VS Code (lo más simple)

Con la extensión *Extension Pack for Java* instalada, abrir cualquier archivo de test
y usar el botón ▶ al lado de la clase o de cada método. También aparecen todos juntos
en el panel **Testing** (el ícono del matraz en la barra lateral).

### Con Maven

```bash
mvn test
```

### Con Docker (si no tenés Maven instalado)

Desde `DonaTrack/servicio-notificaciones`:

```bash
docker run --rm -v "%cd%:/app" -v "%USERPROFILE%/.m2:/root/.m2" -w /app maven:3.9-eclipse-temurin-23 mvn test
```

## El test de envío real

`EnvioRealDeMailIT` **manda un mail de verdad**. Arma el mismo pedido que envía
`servicio-donaciones` (un destinatario con su contacto de tipo `MAIL`) y lo pasa por el
`Notificador`, así recorre toda la cadena hasta la API de Brevo.

La casilla de destino está fija en la constante `MAIL_DEL_CLIENTE` dentro del test.
Para probar con otra, se cambia ahí.

Corre **solo si están las credenciales en el entorno**. Si no están, se saltea, para que
cualquiera pueda clonar el repo y correr `mvn test` en verde sin cuenta de Brevo.

Desde PowerShell, parado en `servicio-notificaciones`:

```bash
$env:BREVO_API_KEY="xkeysib-..."; $env:BREVO_REMITENTE="ftisman@frba.utn.edu.ar"; mvn test -Dtest=EnvioRealDeMailIT
```

Las credenciales son las mismas que están en `DonaTrack/.env`.

Por el sufijo `IT`, Surefire no lo toma en un `mvn test` normal: hay que pedirlo con
`-Dtest=EnvioRealDeMailIT`. Si se quisiera que corra en cada `mvn test`, hay que
renombrarlo a `EnvioRealDeMailTest` — con el costo de un mail real por cada corrida.
