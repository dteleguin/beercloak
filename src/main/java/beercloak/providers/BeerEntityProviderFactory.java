package beercloak.providers;

import org.jboss.logging.Logger;
import org.keycloak.Config.Scope;
import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;
import org.keycloak.connections.jpa.entityprovider.JpaEntityProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;

import beercloak.BeerManager;

public class BeerEntityProviderFactory
    implements JpaEntityProviderFactory
{

    protected static final String ID  = "beer-entity-provider";
    private static final Logger   LOG = Logger.getLogger(BeerEntityProviderFactory.class);

    @Override
    public JpaEntityProvider create(KeycloakSession session)
    {
        LOG.debug("BeerEntityProviderFactory::create");
        return new BeerEntityProvider();
    }

    @Override
    public String getId()
    {
        return ID;
    }

    @Override
    public void init(Scope config)
    {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory)
    {
        factory.register((event) -> {
            if (event instanceof RealmModel.RealmRemovedEvent)
                realmRemoved((RealmModel.RealmRemovedEvent)event);
        });
    }

    @Override
    public void close()
    {
    }

    private void realmRemoved(RealmModel.RealmRemovedEvent event)
    {
        new BeerManager(event.getKeycloakSession()).removeAllBeer();
    }

}
