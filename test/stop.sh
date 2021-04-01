#!/bin/bash
# get keycloak version from pom
KEYCLOAK_VERSION=$(mvn help:evaluate -Dexpression=keycloak.version -q -DforceStdout)

# start docker
KEYCLOAK_VERSION=$KEYCLOAK_VERSION docker-compose down