package beercloak;

import static beercloak.BeerAdminAuth.ROLE_MANAGE_BEER;
import static beercloak.BeerAdminAuth.ROLE_VIEW_BEER;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
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
import org.keycloak.provider.ProviderEvent;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resource.RealmResourceProviderFactory;

/**
 * @author <a href="mailto:mitya@cargosoft.ru">Dmitry Telegin</a>
 */
public class BeerResourceProviderFactory implements RealmResourceProviderFactory {

    private static final String ID = "beer";
    private static final Logger LOG = Logger.getLogger(BeerResourceProviderFactory.class);

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void close() {
    }

    @Override
    public BeerResourceProvider create(KeycloakSession session) {
        EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
        return new BeerResourceProvider(session, em);
    }

    @Override
    public void init(Config.Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

        LOG.debug("BeerResourceProviderFactory::postInit");

        /*
        Workaround for https://issues.jboss.org/browse/KEYCLOAK-5132
        To be able to use transactions from within ProviderFactory::postInit, one has to start a separate thread.
        In order to be able to access Infinispan (via JNDI), this has to be a managed thread.
        This however doesn't apply to the hot (re)deployment case.
        */
        Runnable task = () -> {
            KeycloakModelUtils.runJobInTransaction(factory, (KeycloakSession session) -> {
                ClientModel client;
                List<RealmModel> realms = session.realms().getRealms();
                RealmManager manager = new RealmManager(session);
                for (RealmModel realm : realms) {
                    client = realm.getMasterAdminClient();
                    if (client.getRole(ROLE_VIEW_BEER) == null && client.getRole(ROLE_MANAGE_BEER) == null)
                        addMasterAdminRoles(manager, realm);
                    if (!realm.getName().equals(Config.getAdminRealm())) {
                        client = realm.getClientByClientId(manager.getRealmAdminClientId(realm));
                        if (client.getRole(ROLE_VIEW_BEER) == null && client.getRole(ROLE_MANAGE_BEER) == null)
                            addRealmAdminRoles(manager, realm);
                    }
                }
            });
        };

        try {
            ThreadFactory mtf = (ThreadFactory) new InitialContext().lookup("java:comp/DefaultManagedThreadFactory");
            LOG.debug("Server startup, using a dedicated thread");
            mtf.newThread(task).start();
        } catch (NamingException ex) {
            LOG.debug("Hot (re)deploy, using current thread");
            task.run();
        }

        factory.register((ProviderEvent event) -> {
            if (event instanceof RealmModel.RealmPostCreateEvent) {
                RealmModel.RealmPostCreateEvent postCreate = (RealmModel.RealmPostCreateEvent) event;
                RealmModel realm = postCreate.getCreatedRealm();
                RealmManager manager = new RealmManager(postCreate.getKeycloakSession());
                addMasterAdminRoles(manager, realm);
                if (!realm.getName().equals(Config.getAdminRealm()))
                    addRealmAdminRoles(manager, realm);
            }
        });

    }

    private void addMasterAdminRoles(RealmManager manager, RealmModel realm) {

        RealmModel master = manager.getRealmByName(Config.getAdminRealm());
        RoleModel admin = master.getRole(AdminRoles.ADMIN);
        ClientModel client = realm.getMasterAdminClient();

        addRoles(client, admin);

    }

    private void addRealmAdminRoles(RealmManager manager, RealmModel realm) {

        ClientModel client = realm.getClientByClientId(manager.getRealmAdminClientId(realm));
        RoleModel admin = client.getRole(AdminRoles.REALM_ADMIN);

        addRoles(client, admin);

    }

    private void addRoles(ClientModel client, RoleModel parent) {

        String[] names = new String[] { ROLE_VIEW_BEER, ROLE_MANAGE_BEER };

        for (String name : names) {
            RoleModel role = client.addRole(name);
            role.setDescription("${role_" + name + "}");
            parent.addCompositeRole(role);
        }

    }

}
