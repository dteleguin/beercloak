package beercloak.resources;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class BeerTypeResource
{

    private static final String[] BEER_TYPES = {"Pilsener", "Lager", "Ale", "Porter", "Stout"};

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> list()
    {
        return Arrays.asList(BEER_TYPES);
    }

}
