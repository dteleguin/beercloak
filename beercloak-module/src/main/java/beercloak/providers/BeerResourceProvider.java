package beercloak.providers;

import beercloak.resources.BeerResource;
import javax.persistence.EntityManager;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

/**
 * @author <a href="mailto:mitya@cargosoft.ru">Dmitry Telegin</a>
 */
public class BeerResourceProvider implements RealmResourceProvider {

    private final KeycloakSession session;
    private final EntityManager em;

    public BeerResourceProvider(KeycloakSession session, EntityManager em) {
        this.session = session;
        this.em = em;
    }

    @Override
    public void close() {
    }

    @Override
    public Object getResource() {

        BeerResource beer = new BeerResource(em);
        ResteasyProviderFactory.getInstance().injectProperties(beer);
        beer.setup();
        return beer;

    }

}
