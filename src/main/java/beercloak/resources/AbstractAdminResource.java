package beercloak.resources;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.AccessToken;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.AdminEventBuilder;

/**
 * @author <a href="mailto:mitya@cargosoft.ru">Dmitry Telegin</a>
 * @param <T> type of AdminAuth
 */
public abstract class AbstractAdminResource<T extends AdminAuth> {

    private static final Logger LOG = Logger.getLogger(AbstractAdminResource.class);

    @Context
    private HttpHeaders headers;

    @Context
    private KeycloakSession session;

    protected T auth;
    protected AdminEventBuilder adminEvent;

    public void setup() {
        setupAuth();
        setupEvents();
    }

    private void setupAuth() {

        AppAuthManager authManager = new AppAuthManager();
        String tokenString = authManager.extractAuthorizationHeaderToken(headers);

        if (tokenString == null) {
            throw new NotAuthorizedException("Bearer");
        }

        AccessToken token;

        try {
            JWSInput input = new JWSInput(tokenString);
            token = input.readJsonContent(AccessToken.class);
        } catch (JWSInputException e) {
            throw new NotAuthorizedException("Bearer token format error");
        }

        String realmName = token.getIssuer().substring(token.getIssuer().lastIndexOf('/') + 1);
        RealmManager realmManager = new RealmManager(session);
        RealmModel realm = realmManager.getRealmByName(realmName);

        if (realm == null) {
            throw new NotAuthorizedException("Unknown realm in token");
        }

        AuthenticationManager.AuthResult authResult = authManager.authenticateBearerToken(session, realm);

        if (authResult == null) {
            throw new NotAuthorizedException("Bearer");
        }

        ClientModel client
                = realm.getName().equals(Config.getAdminRealm())
                ? session.getContext().getRealm().getMasterAdminClient()
                : realm.getClientByClientId(realmManager.getRealmAdminClientId(realm));

        UserModel user = authResult.getUser();

        Class clazz = (Class)((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        try {
            Constructor<? extends Type> constructor = clazz.getConstructor(RealmModel.class, AccessToken.class, UserModel.class, ClientModel.class);
            auth = (T) constructor.newInstance(new Object[] { realm, token, user, client });
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.error("Failed to instantiate AdminAuth instance", ex);
        }

    }

    private void setupEvents() {
        adminEvent = new AdminEventBuilder(session.getContext().getRealm(), auth, session, session.getContext().getConnection())
                .realm(session.getContext().getRealm());
    }

}
