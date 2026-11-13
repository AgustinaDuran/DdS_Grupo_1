FROM quay.io/keycloak/keycloak:26.8.2

COPY keycloak/donatrack-realm.json /opt/keycloak/data/import/donatrack-realm.json

# Directorio de la base embebida (H2). Se crea acá, con el usuario de Keycloak como dueño, para que un
# volumen montado en esta ruta herede los permisos y Keycloak pueda escribir (ver docker-compose.deploy.yml).
USER root
RUN mkdir -p /opt/keycloak/data/h2 && chown -R 1000:0 /opt/keycloak/data/h2
USER 1000

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
CMD ["start-dev", "--import-realm"]
