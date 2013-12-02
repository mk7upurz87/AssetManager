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

    @Required()
    public String label;
    
    public String brand;
    
    @Required()
    @Min(value=1)
    public Long quantity;
    
    @Required
    public String description;

    public static Finder<Long, Part> find = new Finder(Long.class, Part.class);



    public static List<Part> all() {
        return find.all();
    }

    public static void create(Part part, String desc) {
        part.save();
        part.description = desc;
    }

    public static void delete(Long id) {
        find.byId(id).delete();
    }
}
