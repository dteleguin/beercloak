# BeerCloak: a comprehensive Keycloak extension example

BeerCloak is a collection of different techniques for building custom admin resources in Keycloak.

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

The `beercloak.resources.AbstractAdminResource` is ready to be used as a base class for admin resources. It contains the code necessary to setup authorization and logging.

### Structure

`beercloak-core`: "core" module with some "business logic", to demonstrate packaging with dependencies  
`beercloak-module`: main module actually containing providers and everything (depends on `beercloak-core`)  
`beercloak-ear`: EAR packaging module to combine all the above into a deployable EAR 

## Requirements

* Keycloak 3.4.0.Final

## Build

`mvn install`

## Installation

1. Copy `beercloak-ear/target/beercloak-XXX.ear` into Keycloak's `standalone/deployments` directory.

**Warning!** While Keycloak generally supports hot deployment of providers, this is *not supported* for EntityProviders.
That means, BeerCloak shouldn't be hot deployed, otherwise you'll get exceptions and non-working code.  
See [KEYCLOAK-5782](https://issues.jboss.org/browse/KEYCLOAK-5782) for more info.

2. Configure theme in your `standalone/configuration/standalone.xml`:
```xml
        <subsystem xmlns="urn:jboss:domain:keycloak-server:1.1">
            ...
            <theme>
                <staticMaxAge>2592000</staticMaxAge>
                <cacheThemes>true</cacheThemes>
                <cacheTemplates>true</cacheTemplates>
                <dir>${jboss.home.dir}/themes</dir>
                <!-- Here we go -->
                <modules>
                    <module>
                        deployment.beercloak
                    </module>
                </modules>
                <default>beer</default>
            </theme>
            ...
        </subsystem>
 ```
 
 You can omit `<default>beer</default>`, but then you'll have to manually choose the "beer" theme in realm configuration → Themes → Admin console theme.
 
(Currently, if you ship a theme inside your module, you have to configure it manually in the XML config. This may change in the future with automatic deployment of themes, you can track progress under [KEYCLOAK-4547](https://issues.jboss.org/browse/KEYCLOAK-4547))

## Running example

Run Keycloak and log into the admin console. You should be able to access the "Beer" menu item.
