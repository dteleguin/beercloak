package beercloak.providers;

import jakarta.persistence.EntityManager;

import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.resource.RealmResourceProvider;

import beercloak.resources.BeerResource;

public class BeerResourceProvider
    implements RealmResourceProvider
{

    private final KeycloakSession session;
    private final EntityManager   em;

    public BeerResourceProvider(KeycloakSession session, EntityManager em)
    {
        this.session = session;
        this.em = em;
    }

    @Override
    public void close()
    {
    }

    @Override
    public Object getResource()
    {

        RealmModel realm = session.getContext().getRealm();
        BeerResource beer = new BeerResource(realm, em);
        ResteasyProviderFactory.getInstance().injectProperties(beer);
        return beer;

    }

}
