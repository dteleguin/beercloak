package beercloak.providers;

import java.util.Collections;
import java.util.List;

import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;

import beercloak.models.jpa.entities.BeerEntity;

public class BeerEntityProvider
    implements JpaEntityProvider
{

    @Override
    public List<Class<?>> getEntities()
    {
        return Collections.<Class<?>>singletonList(BeerEntity.class);
    }

    @Override
    public String getChangelogLocation()
    {
        return "META-INF/jpa-changelog-beercloak.xml";
    }

    @Override
    public void close()
    {
    }

    @Override
    public String getFactoryId()
    {
        return BeerEntityProviderFactory.ID;
    }
}
