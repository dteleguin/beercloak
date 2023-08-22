FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION}
COPY ./target/beercloak-docker-${KEYCLOAK_VERSION}.jar /opt/keycloak/providers/beercloak.jar
RUN /opt/keycloak/bin/kc.sh build --metrics-enabled=true