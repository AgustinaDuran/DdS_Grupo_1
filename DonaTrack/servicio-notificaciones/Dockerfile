# Dockerfile
FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /app
COPY pom.xml .
COPY mensajeria ./mensajeria
COPY servicio-donaciones/pom.xml ./servicio-donaciones/pom.xml
COPY servicio-incentivos/pom.xml ./servicio-incentivos/pom.xml
COPY servicio-logistica/pom.xml ./servicio-logistica/pom.xml
COPY servicio-notificaciones/pom.xml ./servicio-notificaciones/pom.xml
COPY servicio-notificaciones/src ./servicio-notificaciones/src
RUN mvn -pl servicio-notificaciones -am clean package -DskipTests

FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=build /app/servicio-notificaciones/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
