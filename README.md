# BeerCloak: a comprehensive Keycloak extension example

---
**TL;DR**

Use `./test/start.sh` to run the project.

---

BeerCloak is a collection of different techniques for building custom admin resources in Keycloak.

* `BeerEntity` JPA entity + Liquibase changelog
* `BeerResource` realm REST resource with CRUD operations & more
* Authorization:
  * roles: `view-beer` and `manage-beer`
  * automatically created for each existing realm
  * automatically created for each newly added realm
  * automatically included into the master `admin` role
  * ~~used for authorization on `BeerResource` and sub-resources~~
* GUI extensions to the admin console

### Structure

`beercloak-core`: "core" module with some "business logic", to demonstrate packaging with dependencies  
`beercloak-module`: main module actually containing providers and everything (depends on `beercloak-core`)  
`beercloak-ear`: EAR packaging module to combine all the above into a deployable EAR 

## Requirements

* Keycloak 12.0.4+

## Build

`mvn install`

## Installation

Copy `beercloak-ear/target/beercloak-XXX.ear` into Keycloak's `standalone/deployments` directory.

## Running example

Run Keycloak and log into the admin console. You should be able to access the "Beer" menu item.
