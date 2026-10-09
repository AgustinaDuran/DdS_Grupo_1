# Trabajo Práctico Anual: DonaTrack
### Grupo 1
***
Justificaciones de diseño:
Cada entrega tendrá sus justificaciones adjuntadas, aqui se encontrarán las de todas las entregas:
https://docs.google.com/spreadsheets/d/1OR-sQMm7sH_AOQrJBKYCXUq_vbBzcJdW112gkHsQClE

## Ejecutar tests

Desde esta carpeta (`DonaTrack`), ejecutar todos los tests de los cuatro servicios:

```powershell
mvn test
```

Para ejecutar los tests de un servicio individual:

```powershell
mvn -f servicio-donaciones/pom.xml test
mvn -f servicio-incentivos/pom.xml test
mvn -f servicio-notificaciones/pom.xml test
mvn -f servicio-logistica/pom.xml test
```
