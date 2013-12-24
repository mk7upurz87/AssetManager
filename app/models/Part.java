package models;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(
    name="PART", 
    uniqueConstraints=
        @UniqueConstraint(columnNames={"ID"})
)
public class Part extends Model {

	private static final long serialVersionUID = 7739003293639778488L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    @Required(message="You must enter your name for the record")
    public String creator;

    @Required(message="Please enter a division")
    public String division;

    @Required(message="Please enter a valid email address")
    @Email(message="Please enter a valid email address")
    public String email;

    @Required(message="Please enter a valid phone number")
    @Pattern(value="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$",
    		message="example: (555)555-5555, 555-555-5555")
    public String phone;

    @Required(message="Please enter a label for the part")
    public String label;
    
    @Required(message="Please enter a vendor name")
    public String vendor;

    @Required(message="The minimum quantity is 1")
    public Long quantity;
    
    public String description;
    
    public File attachment;
    
    public String attachmentName;
    
    @OneToMany(mappedBy="part", cascade=CascadeType.REMOVE)
    public List<Bid> bids;

    public static Finder<Long, Part> find = new Finder<Long, Part>(Long.class, Part.class);


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
        if(part.attachment != null) {
        	try {
				Files.delete(part.attachment.toPath());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
        }
        part.delete();
    }

    @Override
	public String toString() {
    	String partString = "";
    	partString = partString.concat("\n<p><b>Name:</b>"			+ label 	+ "</p>");
    	partString = partString.concat("\n<p><b>Vendor:</b>"		+ vendor	+ "</p>");
    	partString = partString.concat("\n<p><b>Quantity:</b>"		+ quantity	+ "</p>");
    	if(description != null) {
    		partString = partString.concat("\n<p><b>Description:" 	+ description + "</p>");
    	}
    	partString = partString.concat("\n<p><b>Creator:</b>"		+ creator	+ "</p>");
    	partString = partString.concat("\n<p><b>Division:</b>"		+ division	+ "</p>");
    	partString = partString.concat("\n<p><b>Email:</b>"			+ email		+ "</p>");
    	partString = partString.concat("\n<p><b>Phone:</b>"			+ phone		+ "</p>");
    	
    	return partString;
    }
}
