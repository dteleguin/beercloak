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

**Warning!** Due to some bug probably introduced in KeyCloak 2.2, there can be sporadic exceptions during KeyCloak startup:
```
Caused by: java.sql.SQLException: IJ031017: You cannot set autocommit during a managed transaction
	at org.jboss.jca.adapters.jdbc.BaseWrapperManagedConnection.setJdbcAutoCommit(BaseWrapperManagedConnection.java:994)
	at org.jboss.jca.adapters.jdbc.WrappedConnection.setAutoCommit(WrappedConnection.java:787)
	at org.hibernate.resource.jdbc.internal.AbstractLogicalConnectionImplementor.begin(AbstractLogicalConnectionImplementor.java:67)
	at org.hibernate.resource.jdbc.internal.LogicalConnectionManagedImpl.begin(LogicalConnectionManagedImpl.java:238)
	at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl$TransactionDriverControlImpl.begin(JdbcResourceLocalTransactionCoordinatorImpl.java:214)
	at org.hibernate.engine.transaction.internal.TransactionImpl.begin(TransactionImpl.java:52)
	at org.hibernate.internal.SessionImpl.beginTransaction(SessionImpl.java:1512)
	at org.hibernate.jpa.internal.TransactionImpl.begin(TransactionImpl.java:45)
```
(see full stacktrace [here](http://pastebin.com/ETtPqXQk))

Once you've had a successful startup and the roles have been created, you can comment out the code in `BeerResourceProviderFactory::postInit`.

## Requirements

* KeyCloak 2.3.0

## Build

`mvn install`

## Installation

After the extension has been built, install it as a JBoss/WildFly module via `jboss-cli`:

```
[disconnected /] module add --name=beercloak \
    --resources=/path/to/beercloak-1.0-SNAPSHOT.jar \
    --dependencies=\
        org.keycloak.keycloak-core,\
        org.keycloak.keycloak-model-jpa,\
        org.keycloak.keycloak-server-spi,\
        org.keycloak.keycloak-services,\
        javax.persistence.api,\
        javax.ws.rs.api,\
        org.javassist,\
        org.hibernate,\
        org.jboss.resteasy.resteasy-jaxrs
```

Alternatively, create `$KEYCLOAK_HOME/modules/beercloak/main/module.xml` to load extension from the local Maven repo:

```
<?xml version="1.0" ?>

<module xmlns="urn:jboss:module:1.1" name="beercloak">

    <resources>
        <artifact name="hello:beercloak:1.0-SNAPSHOT"/>
    </resources>

    <dependencies>
        <module name="org.keycloak.keycloak-core"/>
        <module name="org.keycloak.keycloak-model-jpa"/>
        <module name="org.keycloak.keycloak-server-spi"/>
        <module name="org.keycloak.keycloak-services"/>
        <module name="javax.persistence.api"/>
        <module name="javax.ws.rs.api"/>
        <module name="org.javassist"/>
        <module name="org.hibernate"/>
        <module name="org.jboss.resteasy.resteasy-jaxrs"/>
    </dependencies>

</module>
```

## Configuration

`$KEYCLOAK_HOME/standalone/configuration/standalone.xml`:

```
...
        <subsystem xmlns="urn:jboss:domain:keycloak-server:1.1">
            <web-context>auth</web-context>
            <providers>
                <provider>
                    classpath:${jboss.home.dir}/providers/*
                </provider>
                <provider>module:beercloak</provider>
            </providers>
            ...
            <theme>
                ...
                <modules>
                    <module>beercloak</module>
                </modules>
                <default>beercloak</default>
            </theme>
...
```

## Running example

Run KeyCloak and log into the admin console. You should be able to access the "Beer" menu item.
