package models;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import java.util.*;
import javax.persistence.*;
import java.io.*;

@Entity
@Table(
    name="PART", 
    uniqueConstraints=
        @UniqueConstraint(columnNames={"ID"})
)
public class Part extends Model {
  
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    @Required
    public String creator;

    @Required
    public String division;

    @Required
    public String email;

    @Required
    public String phone;

    @Required
    public String label;
    
    @Required
    public String vendor;

    @Required
    public Long quantity;
    
    public String description;

    @OneToMany(mappedBy="part", cascade=CascadeType.REMOVE)
    public List<Bid> bids;

    public static Finder<Long, Part> find = new Finder(Long.class, Part.class);


    public void setDesc(String desc) {
        this.description = desc;
    }

    public static List<Part> all() {
        return find.all();
    }

    public static Part getById(Long id) {
        if(id != null) {
            return Part.find.byId(id);
        }
        return null;
    }

    public static void create(Part p) {
        if(getById(p.id) == null) {
            p.save();
        }
    }

    public static void delete(Long id) {
        Part part = Part.find.byId(id);
        part.delete();
    }

    public String toString() {
        description = description == null ? description : "no desctiption provided.";
        return "Part ID:\t"    + id
            +"\nName: "        + label
            +"\nVendor: "      + vendor
            +"\nQuantity: "    + quantity
            +"\nDescription: " + description + "\n"
            +"\nDivision: "    + division
            +"\nCreator: "     + creator
            +"\nEmail: "       + email + " | "
            +  "Phone: "       + phone;
    }
}
