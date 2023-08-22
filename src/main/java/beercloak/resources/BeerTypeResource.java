package beercloak.resources;

import java.util.Arrays;
import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
