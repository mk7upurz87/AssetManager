package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

@Entity
@Table(
    name="USER", 
    uniqueConstraints=
        @UniqueConstraint(columnNames={"ID", "USERNAME"})
)
public class User extends Model {
  
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    @Required
    // @Min(value=8)
    // @Max(value=40)
    public String password;

    @Required
    // @Max(value=30)
    public String username;

    @Required
    // @Min(value=3)
    // @Max(value=40)
    public String name;

    @Required
    // @Max(value=100)
    public String companyName;

    @OneToMany(mappedBy="user", cascade=CascadeType.REMOVE)
    public List<Bid> bids;

    public static Finder<Long, User> find = new Finder(Long.class, User.class);

    /**
     * 
     */
    public static List<User> all() {
        return find.all();
    }

    /**
     * 
     */
    public static void create(User user) {
        user.save();
    }

    /**
     * 
     */
    public static boolean checkUser(User user) {
        return all().contains(user);
    }

    /**
     * Delete a User
     */
    public static void delete(Long id) {
        User user = User.find.byId(id);
        user.delete();
    }

    /**
     * Retrieve a User from username.
     */
    public static User getByUsername(String username) {
        return find.where().eq("username", username).findUnique();
    }

    /**
     *
     */
    public boolean hasBidOnPart(Long partId) {
        for(Bid bid : bids) {
            if(bid.part.id == partId) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Authenticate a User.
     */
    public static User authenticate(String username, String password) {
        return find.where()
            .eq("username", username)
            .eq("password", password)
            .findUnique();
    }
}
