package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

@Entity
public class Bid extends Model {
  
    @Id
    public Long id;

    @Required
    public Integer value;

    @ManyToOne
    public User user;

    public Part part;

    public String comment;

    public static Finder<Long, Bid> find = new Finder(
        Long.class, Bid.class
    );

    public static List<Bid> all() {
        return find.all();
    }

    public static void create(Bid bid) {
        bid.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }
}
