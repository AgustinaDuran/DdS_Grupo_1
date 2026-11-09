# Plan definitivo del flujo 5: Asignación (backend)

Alcance: los cambios en `servicio-donaciones` para que la persona administradora vea el ranking calculado durante la noche y asigne cada donación a una entidad del ranking (requerimiento 17), marque como vencidas las que correspondan (requerimiento 16), y la entidad beneficiaria vea las donaciones que le asignaron (requerimiento 11).

El frontend sigue la parte B de `plan-flujo-5-asignacion.md`, con los contratos de este documento.

Todas las rutas son relativas a `DonaTrack/servicio-donaciones/src/main/java/org/donatrack/`.

## Decisiones

| # | Decisión | Por qué |
|---|---|---|
| D1 | El ranking **se guarda en la base**, pero **solo el del día**: un resultado por donación, que el job reemplaza cada noche y que se borra cuando la donación se asigna o vence. No se guarda historial. | Los rankings viejos no sirven, pero una lista en memoria se pierde con cada reinicio o redeploy (y con el autorestart de la Entrega 7). Además, no funciona con más de una instancia, deja sin datos a `confirmarDestinoFinal` e impide filtrar y paginar con SQL. Recalcular al arrancar significaría correr los algoritmos en horario de carga. También cumple con la Entrega 5, que pide persistir las entidades del modelo. |
| D2 | La concurrencia se resuelve con **bloqueo optimista (`@Version`)** en `Donacion`. Si dos administradores asignan la misma donación, el segundo recibe un 409. | Los choques son raros, no bloquea filas mientras se lee y no cambia ninguna consulta. |
| D3 | La lista a la que pertenece cada ítem del ranking se guarda como **`varchar` con el nombre del algoritmo**, no como enum. | La consigna prevé algoritmos nuevos. Con un `varchar`, agregar una estrategia no cambia el esquema (principio abierto/cerrado). |
| D4 | **No se desnormaliza la fecha de vencimiento.** Las donaciones pendientes se ordenan por **fecha de ingreso**, la más antigua primero. | La consigna no pide ordenar por vencimiento (el requerimiento 16 solo pide poder marcar una donación como vencida), y lo que entró antes es lo que lleva más tiempo esperando. El requerimiento de desnormalización de la Entrega 5 ya está cubierto en `docs/arquitectura.md`. Así no se toca el alta de donaciones, que es del flujo 3. |
| D5 | Primero se hace lo imprescindible. Las mejoras van en una sección aparte, al final. | Es mucho trabajo para una sola persona. |

---

## Fase 1: Dominio de los algoritmos

- [x] `dominio/algoritmo/AlgoritmoAsignacion`: agregar `String nombre()`, que identifica la lista en la base y en el DTO.
- [x] `dominio/algoritmo/CompatibilidadSemantica`: implementar `nombre()` (`"COMPATIBILIDAD_SEMANTICA"`).
- [x] `dominio/algoritmo/PrioridadSubatendidos`:
  - [x] agregar `@Component`;
  - [x] implementar `nombre()` (`"PRIORIDAD_SUBATENDIDOS"`).
- [x] `dominio/entidadBeneficiaria/EntidadBeneficiaria`: implementar `equals` y `hashCode` por `id`. La intersección usa `HashSet` y no puede depender de que los objetos sean la misma instancia.
- [x] `CoordinadorAsignacion.interseccion`: **orden determinístico**. Ordenar las coincidencias por la suma de sus posiciones en cada ranking (la menor suma primero). Hoy el orden es aleatorio, porque sale de un `HashSet`.
- [x] `ResultadoAsignacion`: inicializar siempre las listas vacías (nunca `null`). Hoy el constructor recibe `null` y `tieneCoincidencias()` puede lanzar NullPointerException.

## Fase 2: Persistencia del ranking

- [x] `ResultadoAsignacion` como `@Entity`:

  ```
  resultados_asignacion
    id                   PK
    donacion_id          FK → donaciones_segmentadas, UNIQUE
    tiene_coincidencias  boolean
    fecha_calculo        datetime
  ```

- [x] Nueva entidad `ItemRankingAsignacion`, relacionada desde `ResultadoAsignacion` con `@OneToMany(cascade = ALL, orphanRemoval = true)`:

  ```
  items_ranking_asignacion
    id            PK
    resultado_id  FK → resultados_asignacion
    entidad_id    FK → entidades_beneficiarias
    lista         varchar(50)   nombre del algoritmo, o "COINCIDENCIAS"
    posicion      int (1..10)
  ```

- [x] Adaptar los getters de `ResultadoAsignacion` (`getCoincidencias()` y `getRankingsPorAlgoritmo()`) para que lean de los ítems. `confirmarDestinoFinal(entidad)` sigue siendo la regla de negocio.
- [x] `repository/ResultadoAsignacionRepository`:
  - [x] `Optional<ResultadoAsignacion> findByDonacionId(Long id)`
  - [x] `List<ResultadoAsignacion> findByDonacionIdIn(Collection<Long> ids)` (para evitar consultas N+1 en el listado)
  - [x] `void deleteByDonacionId(Long id)`

## Fase 3: Coordinador y job

- [x] `CoordinadorAsignacion` como `@Service`:
  - [x] recibir `DonacionesRepository` y obtener las donaciones `EN_DEPOSITO` desde la base (se puede reusar `buscarConFiltros`), en lugar de `Deposito.getInstance()`;
  - [x] recibir la lista de beans `AlgoritmoAsignacion` (Spring inyecta todos los `@Component`);
  - [x] guardar el `ResultadoAsignacion` de cada donación, reemplazando el anterior;
  - [x] `@Transactional`.
- [x] Sacar `Deposito` del flujo de asignación. Si queda sin usos, eliminarlo.
- [x] `JobAsignacion` como `@Component`, con `@Scheduled(cron = "${donaciones.asignacion.cron:0 0 3 * * ?}")`. `@EnableScheduling` ya está activo.
- [x] `application.properties`: agregar `donaciones.asignacion.cron=0 0 3 * * ?`.
- [x] Al empezar el job, borrar los resultados de las donaciones que ya no están `EN_DEPOSITO`.

## Fase 4: Endpoints nuevos

Nuevo `controller/AsignacionesController` con su `service/AsignacionesService`.

- [x] DTO genérico `PaginaDTO<T>` (`contenido`, `pagina`, `tamanio`, `totalElementos`, `totalPaginas`). No conviene serializar `PageImpl` directamente, porque su JSON no es estable.
- [x] `POST /api/asignaciones/ejecuciones`: ejecuta el coordinador a demanda (cumple con la "ejecución a demanda" de la consigna y sirve para probar sin esperar a la noche). Devuelve la cantidad de donaciones procesadas.
- [x] `GET /api/asignaciones/pendientes?page=&size=&subcategoriaId=` → `PaginaDTO<DonacionPendienteDTO>`:
  - [x] solo donaciones `EN_DEPOSITO`;
  - [x] `size` de 50 como máximo;
  - [x] orden: fecha de ingreso, la más antigua primero (y después `id`, para desempatar);
  - [x] traer la página de donaciones y después, **en una sola consulta**, sus resultados (`findByDonacionIdIn`);
  - [x] `ranking.estado = PENDIENTE` cuando la donación todavía no tiene resultado (entró después del último cálculo).

  ```json
  {
    "contenido": [
      {
        "donacionId": 128,
        "descripcion": "Ropa de abrigo",
        "subcategoriaNombre": "Camperas",
        "cantidadTotal": 10,
        "fechaIngreso": "2026-11-05T10:30:00",
        "donanteNombre": "Ana Pérez",
        "ranking": {
          "estado": "CALCULADO",
          "tieneCoincidencias": true,
          "fechaCalculo": "2026-11-07T03:02:00",
          "sugerenciaPrincipal": { "entidadId": 4, "entidadNombre": "Hogar Los Peques" }
        }
      }
    ],
    "pagina": 0,
    "tamanio": 20,
    "totalElementos": 36,
    "totalPaginas": 2
  }
  ```

- [x] `GET /api/asignaciones/pendientes/{donacionId}` → `RankingDonacionDTO`. Si `tieneCoincidencias` es `true`, `listas` trae solo la lista `COINCIDENCIAS`; si no, una lista por algoritmo.

  ```json
  {
    "donacionId": 128,
    "descripcion": "Ropa de abrigo",
    "subcategoriaNombre": "Camperas",
    "cantidadTotal": 10,
    "ranking": {
      "estado": "CALCULADO",
      "fechaCalculo": "2026-11-07T03:02:00",
      "tieneCoincidencias": false,
      "listas": [
        {
          "tipo": "COMPATIBILIDAD_SEMANTICA",
          "entidades": [
            { "posicion": 1, "entidadId": 4, "entidadNombre": "Hogar Los Peques", "direccion": "Av. Siempreviva 742" }
          ]
        },
        { "tipo": "PRIORIDAD_SUBATENDIDOS", "entidades": [] }
      ]
    }
  }
  ```

- [x] `GET /api/entidades-beneficiarias/{id}/donaciones?estado=&page=&size=` → `PaginaDTO<DonacionDTO>`:
  - [x] `estado` se puede repetir y se recibe como `List<TipoEstado>`;
  - [x] orden por defecto: último cambio de estado, el más reciente primero (`estadoDonacion.fechaIngresoEstado`). No existe una columna de "fecha de asignación";
  - [x] es un endpoint nuevo, para no romper `GET /api/donaciones`, que usan otros flujos.

## Fase 5: Asignar, vencer y concurrencia

- [x] `Donacion`: agregar `@Version private Long version;` (sin inicializar).
  - [x] Con `ddl-auto=update`, la columna se crea en `NULL` para las filas existentes, y Spring Data trataría esas donaciones como nuevas. Correr `UPDATE donaciones_segmentadas SET version = 0 WHERE version IS NULL;` una vez, o recrear la base de desarrollo.
- [x] `DonacionesService.actualizarDonacion`: marcarlo `@Transactional`.
- [x] `DonacionesService.cambiarEstadoDonacion`:
  - [x] reemplazar `throw new Error(...)` por `TransicionEstadoInvalidaException`;
  - [x] en `ASIGNACION_REALIZADA`:
    - [x] lanzar `IllegalArgumentException` si falta `entidadId` (400);
    - [x] buscar el `ResultadoAsignacion` y lanzar `RankingNoCalculadoException` si no existe (409);
    - [x] avanzar el estado con `resultado.confirmarDestinoFinal(entidad)`, que valida que la entidad esté en el ranking, en lugar de `donacion.siguiente(entidad)`;
    - [x] borrar el resultado;
  - [x] en `VENCIDA`: borrar el resultado.
- [x] `ResultadoAsignacion.confirmarDestinoFinal`: lanzar `EntidadFueraDelRankingException` en lugar de `IllegalArgumentException`.
- [x] Excepciones nuevas y su código HTTP (clases creadas en `service/excepciones` y `dominio/algoritmo`).
  - [x] Mapearlas en el `@RestControllerAdvice` común (`controller/ManejadorGlobalDeErrores`), con cuerpo `ErrorDTO { status, codigo, mensaje }`:

  | Excepción | HTTP | Mensaje para el toast |
  |---|---|---|
  | `DonacionNoEncontradaException` | 404 | "La donación no existe o fue eliminada." |
  | `TransicionEstadoInvalidaException` | 409 | "Otra persona ya actualizó esta donación." |
  | `ObjectOptimisticLockingFailureException` (de Spring, por `@Version`) | 409 | "Otra persona ya actualizó esta donación." |
  | `RankingNoCalculadoException` | 409 | "Esta donación todavía no tiene ranking." |
  | `EntidadFueraDelRankingException` | 422 | "La entidad elegida no está entre las sugeridas." |
  | `IllegalArgumentException` (falta un parámetro) | 400 | "Falta elegir la entidad." |

## Fase 6: DTO

- [x] `DonacionDTO`: agregar `entidadId`, `entidadNombre`, `donanteNombre` y `cantidadTotal`. Son campos nuevos, así que no rompen a otros flujos.

## Fase 7: Seguridad (depende del flujo 1)

- [x] Dejar un `// TODO flujo 1` en cada punto:
  - `/api/asignaciones/**` y los `PATCH` de asignar o vencer: solo ADMIN;
  - `/api/entidades-beneficiarias/{id}/donaciones`: el backend compara `{id}` con el `entidadId` del token y devuelve 403 si no coinciden.

## Fase 8: Tests

- [x] `CoordinadorAsignacion` con algoritmos falsos: caso con coincidencias (y su orden determinístico) y caso sin coincidencias.
- [x] `ResultadoAsignacion.confirmarDestinoFinal`: entidad válida, entidad fuera del ranking y listas vacías (sin NullPointerException).
- [x] Repositorio: consulta paginada de pendientes, con filtro por subcategoría y orden por fecha de ingreso.
- [x] MockMvc: 400 / 404 / 409 / 422 al asignar.
- [x] Concurrencia: dos asignaciones sobre la misma versión de la donación; la segunda falla con 409.
- [x] Job: guarda los resultados y borra los de donaciones que ya no están en depósito.

## Fase 9: Documentación (entregables)

- [x] Colección de Postman: pasos 26a a 26e de la carpeta 03 y dos casos negativos en la carpeta 07 de `endpoints.json`, más las dos guías de Postman.
- [x] Anotaciones de Swagger (springdoc ya está en el `pom.xml`).
- [ ] Diagrama de clases: `ResultadoAsignacion`, `ItemRankingAsignacion`, `nombre()` en la estrategia y `@Version` en `Donacion`.
- [ ] DER: `resultados_asignacion`, `items_ranking_asignacion` y `version` en `donaciones_segmentadas`.
- [ ] Justificaciones: D1 (por qué en la base y no en memoria), D2 (bloqueo optimista frente a pesimista), D3 (nombre del algoritmo como `varchar`) y D4 (por qué no se desnormalizó el vencimiento).

---

## Opcional (si sobra tiempo)

- [ ] **`motivo` por candidato** (ej.: "Necesita camperas de abrigo (10)", "0 donaciones en el último trimestre"). Es lo de más valor para el administrador. Obliga a que `AlgoritmoAsignacion` devuelva `List<CandidatoRanking>` (entidad + motivo) y suma una columna `motivo` en `items_ranking_asignacion`.
- [ ] **Filtro `soloConCoincidencias`** en `GET /api/asignaciones/pendientes`. Lo usa la tarjeta "Asignaciones sugeridas" del inicio del admin.
- [ ] **Tabla `ejecuciones_asignacion`** (inicio, fin, donaciones procesadas, OK o ERROR) y `GET /api/asignaciones/ejecuciones/ultima`, para mostrar "Último cálculo: hoy 03:02".
- [ ] **Job por lotes** (`Slice` de 200, una transacción por lote), para no cargar todas las donaciones en memoria.
- [ ] **`historialEstados`** en `DonacionDTO` (`[{ estado, fecha, detalle }]`), para la línea de tiempo del detalle de la entidad.
- [ ] **Auditoría de la elección:** guardar en `EstadoAsignacionRealizada` la posición que ocupaba la entidad elegida y si era una coincidencia.
