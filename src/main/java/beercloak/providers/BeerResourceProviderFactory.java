package beercloak.providers;

import static beercloak.resources.BeerAdminAuth.ROLE_MANAGE_BEER;
import static beercloak.resources.BeerAdminAuth.ROLE_VIEW_BEER;
import java.util.List;
import jakarta.persistence.EntityManager;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.AdminRoles;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.models.utils.PostMigrationEvent;
import org.keycloak.provider.ProviderEvent;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resource.RealmResourceProviderFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class BeerResourceProviderFactory
    implements RealmResourceProviderFactory
{

    private static final String ID  = "beer";
    private static final Logger LOG = Logger.getLogger(BeerResourceProviderFactory.class);

    @Override
    public String getId()
    {
        return ID;
    }

    @Override
    public void close()
    {
    }

    @Override
    public BeerResourceProvider create(KeycloakSession session)
    {
        LOG.debug("BeerResourceProviderFactory::create");
        EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
        return new BeerResourceProvider(session, em);
    }

    @Override
    public void init(Config.Scope config)
    {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory)
    {

        LOG.debug("BeerResourceProviderFactory::postInit");

        /*
        Depending on how we are deployed, we need to access data model differently.
        
        When cold deployed (i.e. provider is either present at "deployments" subdirectory or deployed as a JBoss module),
        postInit is invoked too early, specifically before initial realm population/migration.
        In this case we should wait for PostMigrationEvent first.
        
        When hot deployed, PostMigrationEvent won't arrive, so we can do stuff right away.
        
        NB: hot deployment is NOT yet supported for EntityProviders!
        Thus, hot deploying BeerCloak will result in exceptions and non-working code.
        This code is here only to demonstrate correct postInit implementation for all deployment modes.
        See https://issues.jboss.org/browse/KEYCLOAK-5782 for more.
        */

        if (isHotDeploying()) {
            LOG.debug("Hot (re)deploy, using current thread");
            KeycloakModelUtils.runJobInTransaction(factory, this::initRoles);
        }
        else {
            LOG.debug("Server startup, waiting for PostMigrationEvent");
        }

        factory.register((ProviderEvent event) -> {
            if (event instanceof RealmModel.RealmPostCreateEvent)
                realmPostCreate((RealmModel.RealmPostCreateEvent)event);
            else if (event instanceof PostMigrationEvent)
                KeycloakModelUtils.runJobInTransaction(factory, this::initRoles);
        });

    }

    private boolean isHotDeploying()
    {

        /*
        At the moment there's no standard way to determine if we are being cold or hot deployed.
        One of the ad-hoc methods is to check for JNDI presence/absence.
        Another methods include querying current thread name and RESTEasy features.
        
        See discussion: http://lists.jboss.org/pipermail/keycloak-dev/2017-July/009639.html
        */

        try {
            // JNDI present, we're invoked from an application thread, that means cold deployment
            new InitialContext().lookup("java:comp");
            return false;
        } catch (NamingException ex) {
            // JNDI absent, server thread, hot deployment
            return true;
        }

    }

    private void initRoles(KeycloakSession session)
    {

        LOG.debug("BeerResourceProviderFactory::initRoles");

        ClientModel client;
        List<RealmModel> realms = session.realms().getRealmsStream().toList();
        RealmManager manager = new RealmManager(session);
        for (RealmModel realm : realms) {
            client = realm.getMasterAdminClient();
            if (client.getRole(ROLE_VIEW_BEER) == null && client.getRole(ROLE_MANAGE_BEER) == null) {
                addMasterAdminRoles(manager, realm);
            }
            if (!realm.getName().equals(Config.getAdminRealm())) {
                client = realm.getClientByClientId(manager.getRealmAdminClientId(realm));
                if (client.getRole(ROLE_VIEW_BEER) == null && client.getRole(ROLE_MANAGE_BEER) == null) {
                    addRealmAdminRoles(manager, realm);
                }
            }
        }
    }

    private void realmPostCreate(RealmModel.RealmPostCreateEvent event)
    {
        RealmModel realm = event.getCreatedRealm();
        RealmManager manager = new RealmManager(event.getKeycloakSession());
        addMasterAdminRoles(manager, realm);
        if (!realm.getName().equals(Config.getAdminRealm()))
            addRealmAdminRoles(manager, realm);
    }

    private void addMasterAdminRoles(RealmManager manager, RealmModel realm)
    {

        RealmModel master = manager.getRealmByName(Config.getAdminRealm());
        RoleModel admin = master.getRole(AdminRoles.ADMIN);
        ClientModel client = realm.getMasterAdminClient();

        addRoles(client, admin);

    }

    private void addRealmAdminRoles(RealmManager manager, RealmModel realm)
    {

        ClientModel client = realm.getClientByClientId(manager.getRealmAdminClientId(realm));
        RoleModel admin = client.getRole(AdminRoles.REALM_ADMIN);

        addRoles(client, admin);

    }

    private void addRoles(ClientModel client, RoleModel parent)
    {

        String[] names = new String[] {ROLE_VIEW_BEER, ROLE_MANAGE_BEER};

        for (String name : names) {
            RoleModel role = client.addRole(name);
            role.setDescription("${role_" + name + "}");
            parent.addCompositeRole(role);
        }

    }

}
