package controllers;

import play.mvc.*;
import play.api.Play;
import play.api.mvc.Session;
import play.data.*;

import java.util.*;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import models.*;

public class Parts extends Controller {

    static Form<Part> partForm = Form.form(Part.class);

    public static Result index() {
        return ok(views.html.parts_index.render(Part.all(), partForm));
    }

    public static Result newPart() {
        Form<Part> filledForm = partForm.bindFromRequest();
//        MultipartFormData body = request().body().asMultipartFormData();
//        Logger.debug(filledForm.toString());
//        String fileName = null;
//        String contentType = null;
//        String desc = "";
//        FilePart description = body.getFile("description");

//        if (description != null) {
//            fileName = description.getFilename();
//            contentType = description.getContentType(); 
//            File file = description.getFile();
//
//            try {
//                FileReader fr = new FileReader(file);
//                BufferedReader br = new BufferedReader(fr);
//
//                //FileWriter fw = new FileWriter("/../../public/descriptions/" + fileName + ".txt", true);
//                String s;
//                while((s = br.readLine()) != null) {
//                    desc.concat("\n" + s);
//                    // fw.write(s);
//                }
//                fr.close();
//                // fw.close();
//            }
//            catch(FileNotFoundException fnfe) {
//                System.err.println(fnfe.getStackTrace());
//            }
//            catch(IOException ioe) {
//                System.err.println(ioe.getStackTrace());
//            }
//        } else {
//            flash("error", "Missing file");
//            return redirect(routes.Parts.index());    
//        }
        if(filledForm.hasErrors()) {
            return badRequest(
                views.html.parts_index.render(Part.all(), filledForm)
            );
        } else {
            Part part = filledForm.get();
//            if(!part.phone.matches("^\\(\\d{3}\\)\\d{3}-\\d{4}|\\d{3}-\\d{3}-\\d{4}|\\d{10}$")) {
//                return badRequest(
//                    views.html.parts_index.render(Part.all(), filledForm)
//                );
//            }
            // part.setDesc(fileName);

            try {
	            Email email = new SimpleEmail();
	            email.setHostName("smtp.gmail.com");
	            email.setSmtpPort(465);
				email.setFrom("MedTechAM@gmail.com","Asset Manager");
	            email.addTo("mk7upurz87@gmail.com", "Frank Pecora");
	            email.addTo(part.email);
	            email.setSubject("Part Added: " + part.vendor + " - " + part.label);
	            email.setMsg("A Part has been added to the Asset Manager:\n\n"
	                + part.toString());
	            email.addCc("shaundevos668@hotmail.com");
	            email.send();
             
			} catch (EmailException ee) {
				ee.printStackTrace();
			}

            Part.create(part);
            return redirect(routes.Parts.index());
        }
    }

    public static Result deletePart(long id) {
        Part.delete(id);
        List<Bid> bids = Bid.all();
        for(Bid bid : bids) {
            if(bid.part.id == id) {
                bid.delete();
            }
        }
        return redirect(routes.Parts.index());
    }
}
