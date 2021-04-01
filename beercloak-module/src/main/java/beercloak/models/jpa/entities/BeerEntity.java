package beercloak.models.jpa.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.keycloak.models.jpa.entities.UserEntity;

@Entity
@Table(name = "BEER", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"REALM_ID", "NAME"})
})
@NamedQueries({
        @NamedQuery(name = "findBeer", query = "SELECT b FROM BeerEntity b WHERE b.realmId = :realmId AND b.id = :id"),
        @NamedQuery(name = "findBeers", query = "SELECT b FROM BeerEntity b WHERE b.realmId = :realmId AND (lower(b.name) LIKE lower(:search) OR lower(b.type) LIKE lower(:search)) ORDER BY b.name"),
        @NamedQuery(name = "findAllBeers", query = "SELECT b FROM BeerEntity b WHERE b.realmId = :realmId ORDER BY b.name"),
        @NamedQuery(name = "removeAllBeers", query = "DELETE FROM BeerEntity b WHERE b.realmId = :realmId")
})
public class BeerEntity
    implements Serializable
{

    private static final long            serialVersionUID = 1L;

    @Id
    @Column(name = "ID", length = 36)
    @Access(AccessType.PROPERTY) // we do this because relationships often fetch id, but not entity.  This avoids an extra SQL
    protected String                     id;

    @Column(name = "NAME")
    private String                       name;

    @Column(name = "TYPE")
    private String                       type;

    @Column(name = "ABV")
    private float                        abv;

    @Column(name = "REALM_ID")
    protected String                     realmId;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final Collection<UserEntity> drinkers         = new ArrayList<>();

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

    public float getAbv()
    {
        return abv;
    }

    public void setAbv(float abv)
    {
        this.abv = abv;
    }

    public String getRealmId()
    {
        return realmId;
    }

    public void setRealmId(String realmId)
    {
        this.realmId = realmId;
    }

    public Collection<UserEntity> getDrinkers()
    {
        return drinkers;
    }

    @Override
    public boolean equals(Object o)
    {

        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof BeerEntity)) {
            return false;
        }

        return id.equals(((BeerEntity)o).id);

    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public String toString()
    {
        return "beercloak.models.jpa.entities.BeerEntity[ id=" + id + " ]";
    }

}
