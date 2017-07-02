# BeerCloak: a comprehensive KeyCloak extension example

BeerCloak is a collection of different techniques for building custom admin resources in KeyCloak.

* `BeerEntity` JPA entity + LiquiBase changelog;
* `BeerResource` realm REST resource with CRUD operations & more;
* Authorization:
  * roles: `view-beer` and `manage-beer`;
  * automatically created for each existing realm;
  * automatically created for each newly added realm;
  * automatically included into the master `admin` role;
  * used for authorization on `BeerResource` and sub-resources;
* Event logging:
  * AdminEventBuilder instance;
  * custom resource and action types (not yet implemented)
* GUI extensions to the admin console.

The code to setup authorization and logging has been moved into the AbstractAdminResource class. It is ready to be used as a base class for admin resources.

## Requirements

* KeyCloak 3.1.0.Final

## Build

`mvn install`

## Installation

Just drop the `target/beercloak-XXX.jar` into the `standalone/deployments` directory.

## Running example

Run KeyCloak and log into the admin console. You should be able to access the "Beer" menu item.
