FROM quay.io/keycloak/keycloak:26.8.2

COPY keycloak/donatrack-realm.json /opt/keycloak/data/import/donatrack-realm.json

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
CMD ["start-dev", "--import-realm"]
