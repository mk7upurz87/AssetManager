package models;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import java.util.*;
import javax.persistence.*;
import java.io.*;

@Entity
public class Part extends Model {
  
    @Id
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

    public static Finder<Long, Part> find = new Finder(Long.class, Part.class);


    public Part(Long id, String cr, String div, String em, String ph,
            String la, String ve, Long q, String desc) {
        this.id = id;
        this.creator = cr;
        this.division = div;
        this.email = em;
        this.phone = ph;
        this.label = la;
        this.vendor = ve;
        this.quantity = q;
        this.description = desc;
    }

    public static List<Part> all() {
        return find.all();
    }

    public static Part getById(Long id) {
        return find.byId(id);
    }

    public static void create(Part p, String desc) {
        Part part = new Part(p.id, p.creator, p.division, p.email,
            p.phone, p.label, p.vendor, p.quantity, desc);
        part.save();

    }

    public static void delete(Long id) {
        find.byId(id).delete();
    }

    public String toString() {
        return "Part ID:\t" + this.id
            +"\nName:\t"    + this.label
            +"\nVendor:\t"  + this.vendor
            +"\nQuantity\t" + this.quantity
            +"\nDescription\t" + this.description + "\n"
            +"\nCreator:\t" + this.creator
            +"\nDivision:\t"+ this.division
            +"\nEmail:\t"   + this.email + " | "
            +  "Phone:\t"   + this.phone;
    }
}
