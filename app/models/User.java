package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

@Entity
public class User extends Model {
  
    @Id
    public Long id;

    @Required
    @MinLength(value=8)
    public String password;

    @Required
    public String username;

    @Required
    public String name;

    @Required
    public String companyName;

    @OneToMany(cascade=CascadeType.ALL)
    public List<Bid> bids;

    public static Finder<Long, User> find = new Finder(Long.class, User.class);



    public static List<User> all() {
        return find.all();
    }

    public static void create(User user) {
        user.save();
    }

    public static boolean checkUser(User user) {
        return all().contains(user);
    }

    /**
     * Delete a User
     */
    public static void delete(Long id) {
        find.ref(id).delete();
    }

    /**
     * Retrieve a User from username.
     */
    public static User getByUsername(String username) {
        return find.where().eq("username", username).findUnique();
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
