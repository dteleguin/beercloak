package beercloak;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

public class BeerManager
{

    private final KeycloakSession session;
    private final EntityManager   em;
    private final RealmModel      realm;

    public BeerManager(KeycloakSession session)
    {
        this.session = session;
        this.em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
        realm = session.getContext().getRealm();
    }

    public int removeAllBeer()
    {
        Query query = em.createNamedQuery("removeAllBeers");
        query.setParameter("realmId", realm.getId());
        return query.executeUpdate();
    }

}
