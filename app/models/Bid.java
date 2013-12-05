package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

@Entity
@Table(
    name="BID", 
    uniqueConstraints=
        @UniqueConstraint(columnNames={"ID"})
)
public class Bid extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    @Required
    @Max(value=2147483647)
    @Min(value=0)
    public Integer value;

    @ManyToOne(fetch=FetchType.LAZY)
    public User user;

    @ManyToOne(fetch=FetchType.LAZY)
    public Part part;

    public String comment;

    public static Finder<Long, Bid> find = new Finder(
        Long.class, Bid.class
    );

    public static List<Bid> all() {
        return find.all();
    }

    public static void create(Bid bid) {
        if(bid.user == null || bid.user.id == null) {
            bid.user = null;
        } else {
            bid.user = User.find.byId(bid.user.id);
        }
        if(bid.part == null || bid.part.id == null) {
            bid.part = null;
        } else {
            bid.part = Part.find.byId(bid.part.id);
        }
        bid.part.bids.add(bid);
        bid.user.bids.add(bid);
        bid.save();
    }

    public static void delete(Long id) {
        Bid bid = Bid.find.byId(id);
        bid.user.bids.remove(bid);
        bid.part.bids.remove(bid);
        bid.delete();
    }
}
