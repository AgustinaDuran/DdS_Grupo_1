# DonaTrack: Arquitectura del sistema

**Trabajo Práctico Anual: Diseño de Sistemas, Grupo 1**

Este documento describe la arquitectura de DonaTrack a nivel macro: el estilo arquitectónico, cómo se dividió el sistema, cómo se comunican las partes, cómo se organiza cada una internamente y cómo se despliega. No describe los componentes en detalle (eso está en el diagrama de clases y en las justificaciones de diseño de cada entrega). El foco está en por qué se tomó cada decisión.

---

## 1. Visión general

DonaTrack se construyó como un conjunto de **servicios independientes**, cada uno a cargo de una parte del negocio, que se comunican por HTTP y por mensajería asíncrona.


| Servicio | Responsabilidad |
|---|---|
| **Donaciones** | Núcleo del sistema: donantes, entidades beneficiarias, necesidades, bienes, categorías y el ciclo de vida de cada donación. Es el que coordina con el resto. |
| **Incentivos** | Gamificación: misiones, insignias, categorías de donante y ranking mensual. |
| **Notificaciones** | Envío de mensajes a donantes, entidades y administradores por el medio de contacto de cada uno. |
| **Logística** | Camiones, entregas, planificación de rutas (con un optimizador externo) y seguimiento en tiempo real. |
| **Logística mock** | Simula un proveedor de logística de terceros con una API distinta. Sirve como respaldo del servicio propio. |
| **mensajeria** (librería) | Módulo Maven compartido con la configuración de RabbitMQ y el publicador. No es un servicio. |

---

## 2. Estilo arquitectónico: servicios separados por dominio

### Decisión
Dividir el sistema en servicios desplegables por separado, uno por cada área de negocio del enunciado, en lugar de una única aplicación monolítica.

### Justificación
- **El enunciado ya separa las áreas.** Donaciones, incentivos, notificaciones y logística aparecen como módulos con reglas, actores y ritmos propios. Separarlos en servicios refleja esa división y evita que, por ejemplo, un cambio en las misiones de incentivos obligue a tocar o redesplegar la gestión de donaciones.
- **Requisitos no funcionales distintos.** Logística tiene que exponer un callback público para el optimizador externo y recibir la ubicación de los camiones; Notificaciones depende de un proveedor de mail que puede fallar; Incentivos corre procesos mensuales. Tener cada uno aparte permite desplegarlos, escalarlos y reiniciarlos sin afectar a los demás. De hecho, Logística está desplegado en un hosting propio mientras el resto corre en otro entorno (ver sección 7).
- **Aislamiento de fallas.** Si Notificaciones o Incentivos se caen, una donación igual se puede registrar. Esto se refuerza con la forma de comunicación (sección 3).
- **Trabajo en paralelo del grupo.** Cada integrante pudo avanzar en un servicio con su propio modelo, sus propios tests y su propio `pom.xml`, con pocos puntos de conflicto.

### Alternativa descartada
**Monolito modular** (una sola aplicación Spring Boot con paquetes por dominio). Era más simple de levantar y de depurar, pero no permitía desplegar Logística por separado, hacía que una falla del proveedor de mail o del webhook pudiera afectar el registro de donaciones, y acoplaba los modelos de todos los dominios en un único esquema. Asumimos a cambio el costo de la complejidad operativa: varios procesos, una red de contenedores, configuración de URLs entre servicios y consistencia eventual entre ellos.

### Granularidad
No se partió más fino (por ejemplo, un servicio para categorías y otro para bienes) porque esas entidades se usan siempre junto con las donaciones y separarlas solo hubiera agregado llamadas remotas sin beneficio. El criterio fue: **un servicio por área de negocio con reglas y actores propios**.

---

## 3. Comunicación entre servicios

Se usan tres mecanismos, elegidos según lo que necesita cada interacción.

### 3.1 HTTP/REST síncrono
**Dónde:** Donaciones → Incentivos (informar una donación) y Donaciones → Logística (registrar y consultar entregas).

**Por qué:** son pedidos puntuales en los que Donaciones necesita saber si el otro lado aceptó la operación (por ejemplo, el broker de logística pasa al siguiente proveedor si el primero falla). REST es simple, se prueba fácil con Postman y está documentado con OpenAPI/Swagger en cada servicio.

**Cómo se acota el acoplamiento:**
- Las llamadas a servicios **accesorios** (Incentivos) se envuelven en manejo de errores: si fallan, se registra en el log y el flujo principal sigue. Una donación no se pierde porque Incentivos no responda.
- Los clientes HTTP hacia logística tienen **timeouts explícitos** (3 s de conexión, 5 s de lectura). Sin ellos, un proveedor que acepta la conexión y no responde dejaría al sistema esperando indefinidamente.
- Las URLs de los otros servicios se inyectan por configuración (variables de entorno), no están fijas en el código.

### 3.2 Mensajería asíncrona con RabbitMQ
**Dónde:** Donaciones e Incentivos → Notificaciones.

**Por qué:**
- Notificar es una tarea **de "disparar y olvidar"**: quien genera el evento no necesita esperar a que el mail salga.
- El envío depende de un proveedor externo (Brevo) que puede ser lento o no estar disponible. Con una cola, si Notificaciones está caído los mensajes quedan esperando y se procesan cuando vuelve; con HTTP directo se perderían o bloquearían al emisor.
- Hay **varios productores** (Donaciones e Incentivos) y un único consumidor. La cola desacopla a los productores de quién y cómo consume.

**Decisiones de configuración:**
- *Exchange* de tipo *direct* con una cola **durable**, para que los mensajes sobrevivan a un reinicio del broker.
- **Dead-letter queue**: los mensajes que no se pueden procesar se derivan a una cola aparte en lugar de reintentarse infinitamente o descartarse, para poder revisarlos.
- Mensajes en **JSON**, para no atar a los servicios a clases Java compartidas en el formato de intercambio.
- La configuración de RabbitMQ vive en el módulo mensajeria, que reutilizan los tres servicios. Así los nombres de exchange, cola y routing key se definen una sola vez y no pueden quedar desalineados entre productor y consumidor.

**Alternativa descartada:** llamar a Notificaciones por HTTP (era la forma inicial). Se reemplazó por la cola por los problemas de disponibilidad y bloqueo mencionados.

### 3.3 Polling y callbacks para integrar sistemas que no pueden llamarnos
**Polling: Donaciones consulta a Logística.** Por la consigna, Logística no puede invocar a Donaciones ni a Notificaciones. Por lo tanto, Donaciones consulta periódicamente (cada 60 s, configurable) el estado de las entregas, detecta cambios respecto del estado local de cada donación y, si hay uno, sincroniza el estado y dispara la notificación (inicio de recorrido, entrega exitosa o fallida). Se eligió polling porque respeta la restricción de dependencias sin agregar infraestructura. El costo es una demora de hasta un intervalo en enterarse del cambio, aceptable para este dominio. El proceso es **idempotente**: si el estado ya está sincronizado no vuelve a notificar.

**Callback: el optimizador externo responde a Logística.** La planificación de rutas puede tardar, así que Logística envía la solicitud y el optimizador devuelve el resultado a un endpoint público callback-planificador. Esto evita mantener una conexión abierta esperando y es el contrato que propone el proveedor externo.

**Webhook saliente: Incentivos → Make.** Para publicar logros en redes se usa un webhook de Make, en vez de integrar cada red social. Es accesorio, con timeouts cortos (2 s / 3 s) y errores tolerados, para que nunca frene el flujo principal.

---

## 4. Integración con proveedores de logística: Broker + Adapters

### Decisión
Donaciones no habla directamente con un proveedor de logística sino con un **broker** BrokerLogistica. El broker conoce una lista ordenada de proveedores, cada uno detrás de una interfaz común ProveedorLogistica implementada por un adapter:
- un adapter para nuestro Servicio de Logística (primero en orden, ya habla el formato de DonaTrack), y
- un adapter para el proveedor externo, que tiene otra API (rutas, nombres de campos y estados en inglés) y traduce en ambos sentidos.

Al registrar entregas, el broker hace **failover**: prueba los proveedores en orden y se queda con el primero que responde. Al consultar, pregunta a todos, porque por el failover una entrega pudo haber quedado en cualquiera.

### Justificación
- **Disponibilidad:** si nuestro servicio de logística (desplegado en un hosting aparte) no está disponible, las donaciones igual pueden despacharse por el proveedor alternativo.
- **Aislar el modelo propio de APIs ajenas (Adapter):** el resto de Donaciones trabaja siempre con sus propios DTOs. Los formatos de cada proveedor quedan encerrados en su adapter.
- **Extensibilidad:** sumar un proveedor nuevo es agregar una clase que implemente la interfaz. El broker y el resto del servicio no cambian (principio abierto/cerrado). Spring inyecta automáticamente todos los adapters disponibles en el orden indicado.
- **Reintentos seguros:** el proveedor externo no crea un envío duplicado si recibe dos veces la misma donación (por ejemplo, tras un timeout en el que el envío sí había llegado).

---

## 5. Arquitectura interna de cada servicio: capas

Todos los servicios siguen la misma organización en capas, para que cualquier integrante pueda moverse entre servicios sin tener que reaprender la estructura:


controller   → API REST: recibe requests, valida y convierte DTOs  ⇄  dominio
service      → casos de uso: orquesta dominio, repositorios e integraciones
dominio/model→ entidades y reglas de negocio
repository   → acceso a datos (Spring Data JPA)
integracion  → clientes hacia otros servicios / sistemas externos, schedulers


Las dependencias van en un solo sentido: controller → service → dominio, repository, integracion. El dominio no depende de los controllers ni de los clientes HTTP.

### Justificación
- **Separación de responsabilidades:** la lógica de negocio (por ejemplo, las transiciones de estado de una donación o la evaluación de misiones) está en el modelo, no en los controllers, y se puede testear sin levantar la aplicación ni la base.
- **DTOs en el borde:** la API expone DTOs y no las entidades JPA. Así el contrato público no cambia cada vez que se modifica el modelo de persistencia, y no se filtran relaciones internas o datos sensibles.
- **Integraciones en su propia capa:** todo lo que sale del servicio (HTTP, cola, webhooks) está en integracion. Esto concentra el manejo de errores, timeouts y traducción de formatos, y permite reemplazar la tecnología de comunicación sin tocar los casos de uso (de hecho, el paso de HTTP a RabbitMQ en notificaciones solo cambió el cliente).
- **Procesos programados como componentes separados:** el polling de logística, el aviso de inactividad de donantes, la generación del ranking mensual y la planificación diaria de rutas son schedulers que invocan a los servicios. No están mezclados con la lógica de los endpoints.

### Alternativa considerada
**Arquitectura hexagonal (puertos y adaptadores) completa** en cada servicio. Se aplicó la idea donde más valor tenía, en la integración con logística (interfaz + adapters), pero no en todo el sistema: para el tamaño del TP, duplicar interfaces para cada repositorio y caso de uso agregaba mucho código sin un beneficio concreto. Usamos directamente las interfaces de Spring Data como abstracción de la persistencia.

### Patrones de diseño a nivel de modelo
Dentro de cada servicio se aplicaron patrones de diseño para resolver problemas puntuales del dominio: State para el ciclo de vida de la donación, Composite para categorías, Strategy para los algoritmos de asignación y para los medios de envío, Factory para medios de envío e insignias, entre otros. Su justificación está en las planillas de decisiones de diseño de cada entrega.

---

## 6. Persistencia

### Decisiones
- **Base relacional (MySQL 8)** accedida con JPA/Hibernate a través de Spring Data.
- **Cada servicio es dueño de sus propias tablas y no lee las de otro.** Cuando un servicio necesita información de otro, la recibe por la API o por mensajes. Por ejemplo, Incentivos mantiene su propia vista del donante y de sus donaciones, que Donaciones le informa en cada registro, en lugar de consultar las tablas de Donaciones.
- Las jerarquías del modelo (por ejemplo, los estados de una donación o los tipos de donante) se mapean con herencia de JPA para conservar el polimorfismo del dominio en la base.

### Justificación
- El dominio es fuertemente relacional (donaciones, ítems, bienes, categorías, donantes, entidades) y necesita consistencia transaccional dentro de cada servicio, que es lo que da una base relacional.
- JPA permite mapear el modelo de objetos casi sin código de acceso a datos y cambiar de motor si hiciera falta (los tests usan H2 en memoria).
- Que los servicios no compartan tablas evita el principal acoplamiento oculto entre servicios: si Incentivos leyera las tablas de Donaciones, cualquier cambio de esquema en Donaciones lo rompería.

### Base de datos por entorno
- **Desarrollo local:** se usa una única instancia de MySQL con un único esquema para todos los servicios. Simplifica levantar el entorno y consume menos recursos. La separación es lógica: cada servicio usa solo sus tablas.
- **Despliegue de Logística:** tiene su **propia instancia de MySQL**, accesible solo desde la red interna del servicio. Es el modelo al que apunta la arquitectura: una base por servicio.

Se reconoce como **deuda técnica** que en local la separación de datos se respete por convención y no por infraestructura. El siguiente paso sería replicar el esquema de Logística (una base por servicio) en el resto.

---

## 7. Despliegue

### Decisiones
- **Contenedores Docker** para cada servicio, con builds multi-stage: una etapa compila con Maven y la imagen final solo contiene el JRE y el .jar, lo que da imágenes más chicas.
- **Docker Compose** para orquestar el entorno completo en local (servicios, MySQL y RabbitMQ), con healthchecks. Los servicios esperan a que la base y el broker estén realmente listos antes de arrancar.
- **Configuración externa:** URLs, credenciales y claves (por ejemplo, la API key de Brevo) se pasan por variables de entorno o un archivo .env que no se versiona.
- **Proyecto Maven multimódulo:** un pom.xml padre agrupa los servicios y la librería mensajeria, de modo que mvn test corre los tests de todo el sistema.
- **Logística desplegado en un servidor público** (VPS de 512 MB), con su propio docker-compose.deploy.ym que usa imágenes publicadas en Docker Hub. En el servidor solo hacen falta ese archivo y el .env, no el código fuente.

### Justificación
- Logística necesita una **URL pública** para que el optimizador externo le devuelva las rutas por callback, cosa que no se puede hacer desde una máquina local. Por eso es el servicio que se desplegó.
- Por los recursos limitados del servidor se ajustaron la memoria de la JVM y la configuración de MySQL para que ambos entren en 512 MB. El hecho de que cada servicio sea independiente permitió desplegar solo el que lo necesitaba, sin pagar un servidor para todo el sistema.
- Docker garantiza que el entorno sea igual en las máquinas de todos los integrantes y en el servidor.

---

## 8. Tolerancia a fallas: criterios transversales

Más allá de cada decisión puntual, se siguieron los mismos criterios en todo el sistema:

1. **Lo accesorio nunca bloquea lo principal.** Notificaciones, incentivos y publicación en redes pueden fallar sin impedir que se registre una donación.
2. **Toda llamada externa tiene timeout.**
3. **Si algo falla, queda registro.** Los mensajes no procesables van a la dead-letter queue. Una notificación que no pudo enviarse se guarda como fallida con el motivo, no se descarta en silencio.
4. **Degradar en lugar de no arrancar.** Si faltan las credenciales del proveedor de mail, Notificaciones arranca igual con un cliente que no envía Null Object, y las notificaciones quedan registradas como fallidas.
5. **Procesos idempotentes.** El polling no repite notificaciones ya enviadas y el proveedor externo no duplica envíos ante reintentos.

---

## 9. Tecnologías elegidas

| Tecnología | Uso | Motivo |

| Java 23 + Spring Boot 3.5 | Todos los servicios | Lenguaje de la materia. Spring resuelve inyección de dependencias, REST, JPA, schedulers y AMQP con un mismo modelo de programación. |
| Spring Data JPA / Hibernate | Persistencia | Mapeo objeto-relacional del modelo de dominio con mínimo código. |
| MySQL 8 | Base de datos | Relacional, conocida por el grupo y con imagen oficial de Docker. H2 para tests. |
| RabbitMQ | Mensajería | Broker liviano con colas durables y dead-letter listos para usar. Integración nativa con Spring AMQP. |
| springdoc-openapi | Documentación de APIs | Swagger generado desde el código, siempre sincronizado con los endpoints. |
| Docker / Docker Compose | Ejecución y despliegue | Entorno reproducible y despliegue por imágenes. |
| Brevo / Make | Servicios externos | Envío de mails y publicación en redes sin implementar esa infraestructura. |



## 10. Pendientes y limitaciones conocidas

- **Autenticación:** existe el módulo servicio-autenticacion, pero todavía no está implementado ni integrado. La idea es centralizar el login y que los demás servicios validen un token, en lugar de que cada uno gestione credenciales.
- **Base por servicio** en todos los entornos (ver sección 6).
- **Medios de notificación:** la arquitectura soporta mail, WhatsApp y SMS, pero hoy solo el mail envía mensajes reales. Los demás medios se pueden agregar sin cambiar a los productores, que solo publican en la cola.
