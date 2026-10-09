# Guía simple de Postman — DonaTrack

## Antes de empezar

Desde la raíz del proyecto ejecutar:

```bash
docker compose down -v
docker compose up --build
```

Esperar a que estén disponibles:

- Donaciones: `http://localhost:8080`
- Notificaciones: `http://localhost:8081`
- Incentivos: `http://localhost:8082`
- Logística: `http://localhost:8083`

Importar en Postman:

```text
/home/daguerosilva/endpoints.json
```

Ejecutar las requests en este orden. Los scripts guardan automáticamente los IDs.

## Orden exacto

### 1. Catálogo

Carpeta `01 - Donaciones - catálogo`:

1. Crear categoría.
2. Listar categorías. Guarda `categoriaId`.
3. Crear subcategoría.
4. Listar subcategorías. Guarda `subcategoriaId`.
5. Obtener categoría.
6. Actualizar categoría.
7. Actualizar subcategoría.
8. Obtener subcategoría.

### 2. Donantes y entidad

Carpeta `02 - Donantes, entidades e importación`:

9. Crear donante humano.
10. Crear donante jurídico.
11. Listar donantes. Guarda `donanteId` de Ana.
12. Crear entidad beneficiaria.
13. Listar entidades. Guarda `entidadId`.
14. Importar donantes masivamente desde CSV.
15. Verificar importación y guardar `donanteImportadoId`.

El CSV está en:

```text
/home/daguerosilva/Downloads/donantes_import_20000_UTF8_BOM.csv
```

La request usa `multipart/form-data` con un campo llamado `file`. Si Postman no carga automáticamente el archivo, seleccionarlo manualmente.

### 3. Necesidades, bienes y donación

Carpeta `03 - Necesidades, bienes y donación`:

16. Crear necesidad recurrente.
17. Crear necesidad extraordinaria.
18. Listar necesidades. Guarda `necesidadId`.
19. Actualizar necesidad.
20. Crear bien.
21. Listar bienes. Guarda `bienId`.
22. Actualizar bien.
23. Crear donación.
24. Listar donaciones. Guarda `donacionId`.
25. Obtener donación.
26. Asignar donación a entidad.

La creación de donación debe incluir `subcategoriaId` dentro de `items`:

```json
{
  "donanteId": {{donanteId}},
  "descripcion": "Donacion de la planta de pastas",
  "items": [
    {
      "nombre": "Fideos secos",
      "subcategoriaId": {{subcategoriaId}},
      "cantidad": 100,
      "unidad": "GRAMO",
      "cantidadBien": 500,
      "fechaVencimiento": "2027-01-01"
    }
  ]
}
```

El paso 26 envía automáticamente la entrega a Logística.

### 4. Incentivos

Carpeta `04 - Incentivos`:

27. Registrar donación de Ana - junio.
28. Registrar donación de Ana - julio.
29. Registrar donación de Ana - agosto.
30. Perfil analítico de Ana.
31. Progreso de misiones de Ana.
32. Vitrina de Ana.
33. Generar ranking con `periodo=2026-08`.
34. Consultar ranking con `periodo=2026-08`.

### 5. Notificaciones

Antes de ejecutar `Enviar notificación manual`, verificar Brevo:

1. En Brevo, abrir `Security` → `Authorised IPs` y agregar la IP pública desde la que se corre Docker.
2. Si la IP cambia, actualizar nuevamente o desactivar temporalmente la restricción de IP para la prueba.
3. Regenerar la API key de Brevo si fue expuesta y reemplazar `BREVO_API_KEY` en el `.env` de la raíz.
4. Reiniciar el servicio: `docker compose up -d --build servicio-notificaciones`.

Si la IP no está autorizada, Brevo responde `401` y el envío queda `FALLIDA`; ya no debería producir un `500` de base de datos.

Carpeta `05 - Notificaciones`:

35. Enviar notificación manual.
36. Listar historial de notificaciones.

### 6. Iniciar ruta correctamente

Carpeta `06 - Logística - ruta completa`:

37. Crear camión.
38. Listar camiones.
39. Modificar camión.
40. Actualizar ubicación.
41. Listar entregas pendientes. Guarda `entregaId`.
42. Planificar rutas.
43. Ejecutar callback del planificador.
44. Consultar ruta activa y verificar que exista.
45. Iniciar ruta.
46. Consultar entrega en traslado.
47. Marcar entrega como entregada.
48. Listar rutas activas.

No ejecutar `Iniciar ruta` antes del callback. El callback necesita el `entregaId` real guardado en el paso 41.

### 7. Casos negativos

Ejecutar al final la carpeta `07 - Casos negativos`:

49. Donante sin `tipoUsuario`.
50. Donación inexistente.
51. Donante de Incentivos inexistente.
52. Período de ranking inválido.
53. Inicio de ruta sin ruta activa.
54. Estado de entrega inexistente.

Los casos de negocio de Donaciones y Logística pueden responder `500` porque el backend todavía no tiene un manejador global de excepciones. La colección contempla esos códigos actuales.

### 8. Limpieza opcional

Ejecutar al final `08 - Limpieza opcional` si querés borrar los datos creados.

Para reiniciar completamente otra vez:

```bash
docker compose down -v
docker compose up --build
```

## Integraciones opcionales

La carpeta `06b - Integraciones opcionales` no forma parte del orden principal.

Usar `Registrar nuevas entregas manualmente` solamente si se busca probar directamente el endpoint de Logística o si la asignación automática no pudo enviar la entrega. No ejecutarlo en el flujo normal porque crea una entrega adicional.
