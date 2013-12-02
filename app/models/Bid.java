package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

@Entity
public class Bid extends Model {
  
    @Id
    public Long id;

    @ManyToOne
    public User user;

    @Required
    @ManyToOne
    public Part part;

    @Required
    public Integer value;

    public static Finder<Long, Bid> find = new Finder(
        Long.class, Bid.class
    );

    public static List<Bid> all() {
        return find.all();
    }

    public static void create(Bid bid) {
        bid.part = Part.find.byId(bid.part.id);      
        bid.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }
}
