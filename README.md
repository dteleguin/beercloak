# BeerCloak: a comprehensive Keycloak extension example

![beer](https://img.shields.io/badge/beer-flowing-green)
![hops](https://img.shields.io/badge/hops-100%25-green)
![soberness](https://img.shields.io/badge/soberness-none-red)

---
**TL;DR**

Use `./test/start.sh` to run the project. (You will need [certificates](#generating-certificates).)

Go to [id.keycloak.test/auth](https://id.keycloak.test/auth/).

---

BeerCloak is a collection of different techniques for building custom admin resources in Keycloak.

* `BeerEntity` JPA entity + Liquibase changelog
* `BeerResource` realm REST resource with CRUD operations & more
* Authorization:
  * roles: `view-beer` and `manage-beer`
  * automatically created for each existing realm
  * automatically created for each newly added realm
  * automatically included into the master `admin` role
* GUI extensions to the admin console

### Structure

`beercloak-core`: "core" module with some "business logic", to demonstrate packaging with dependencies  
`beercloak-module`: main module actually containing providers and everything (depends on `beercloak-core`)  
`beercloak-ear`: EAR packaging module to combine all the above into a deployable EAR 

## Requirements

* Keycloak 22.0
* Java 17
* something to generate TLS certificates (`mkcert` or `openssl`)

## Generating certificates

### Using a valid certificate

1. Add the test domain to your hosts file (`/etc/hosts`)
   ```
   127.0.0.1 id.keycloak.test
   ```
   
2. Generate the certificates with [`mkcert`](https://github.com/FiloSottile/mkcert)
   ```
   mkcert -install -cert-file cert.pem -key-file cert-key.pem id.keycloak.test 127.0.0.1
   ```
   
3. Rename the certificate and key to `cert.pem` and `cert-key.pem` respectively.

4. Update permissions for the key
   ```
   chmod 755 cert-key.pem
   ```

5. Update `docker-compose.yml` > `KC_HOSTNAME` to:
   ```yaml
   KC_HOSTNAME: id.keycloak.test
   ```

### Invalid certificate

If you don't care about the certificate being valid, you can use `openssl` to generate one.

1. Generate the certificates
   ```
   openssl req -newkey rsa:2048 -nodes -keyout cert-key.pem -x509 -days 3650 -out cert.pem
   ```
   
2. Update permissions for the key
   ```￼
   chmod 755 cert-key.pem
   ```

3. Update `docker-compose.yml` > `KC_HOSTNAME` to:
   ```yaml
   KC_HOSTNAME: localhost
   ```

## Run Beercloak

Execute `test/start.sh` to build the extension and run Keycloak with Maildev.

## Manual build

1. Run `mvn install`
2. Copy `beercloak-ear/target/beercloak-XXX.ear` into Keycloak's `/opt/keycloak/providers` directory.

## Running example

Run Keycloak and log into the admin console. You should be able to access the "Beer" menu item.
