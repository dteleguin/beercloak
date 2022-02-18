#!/bin/bash

# get keycloak version from pom
export KEYCLOAK_VERSION=$(mvn help:evaluate -Dexpression=keycloak.version -q -DforceStdout)

function cleanup {
  printf '\U1F433 %s\n' "Stopping Docker containers"
  KEYCLOAK_VERSION=$KEYCLOAK_VERSION docker-compose down --volumes
}

trap cleanup EXIT

# build extension without SNAPSHOT suffix
mvn clean package -DskipTests -Drevision=docker -Dchangelist=
if [[ "$?" -ne 0 ]] ; then
  echo 'could not run maven package'; exit $rc
fi

# replace keycloak version in Dockerfile
envsubst < Dockerfile > tmp && mv tmp Dockerfile

# start docker
docker-compose up --build
