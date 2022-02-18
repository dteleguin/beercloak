FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION}
RUN /opt/keycloak/bin/kc.sh build --metrics-enabled=true