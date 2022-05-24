package beercloak.representations;

public class BeerRepresentation
{

    protected String self; // link
    protected String id;

    protected String name;
    protected String type;
    protected Float  abv;

    public String getSelf()
    {
        return self;
    }

    public void setSelf(String self)
    {
        this.self = self;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Float getAbv()
    {
        return abv;
    }

    public void setAbv(Float abv)
    {
        this.abv = abv;
    }

}
