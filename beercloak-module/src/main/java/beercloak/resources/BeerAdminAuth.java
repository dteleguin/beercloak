package beercloak.resources;

import javax.ws.rs.NotAuthorizedException;

import org.keycloak.models.ClientModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.AccessToken;
import org.keycloak.services.resources.admin.AdminAuth;

public class BeerAdminAuth extends AdminAuth
{

    public static final String ROLE_VIEW_BEER   = "view-beer";
    public static final String ROLE_MANAGE_BEER = "manage-beer";

    public BeerAdminAuth(RealmModel realm, AccessToken token, UserModel user, ClientModel client)
    {
        super(realm, token, user, client);
    }

    void checkViewBeer()
    {
        if (!hasAppRole(getClient(), ROLE_VIEW_BEER))
            throw new NotAuthorizedException(ROLE_VIEW_BEER);
    }

    void checkManageBeer()
    {
        if (!hasAppRole(getClient(), ROLE_MANAGE_BEER))
            throw new NotAuthorizedException(ROLE_MANAGE_BEER);
    }

}
