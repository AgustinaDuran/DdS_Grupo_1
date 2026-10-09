# Dockerfile
FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /app
COPY pom.xml .
COPY mensajeria ./mensajeria
COPY servicio-donaciones/pom.xml ./servicio-donaciones/pom.xml
COPY servicio-incentivos/pom.xml ./servicio-incentivos/pom.xml
COPY servicio-notificaciones/pom.xml ./servicio-notificaciones/pom.xml
COPY servicio-logistica-mock/pom.xml ./servicio-logistica-mock/pom.xml
COPY servicio-logistica/pom.xml ./servicio-logistica/pom.xml
COPY servicio-logistica/src ./servicio-logistica/src
RUN mvn -pl servicio-logistica -am clean package -DskipTests

FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=build /app/servicio-logistica/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
