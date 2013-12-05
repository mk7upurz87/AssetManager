package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.libs.*;
import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.*;

import java.io.*;
import java.util.*;

// import org.apache.commons.mail.*;

import views.html.*;
import models.*;

public class Parts extends Controller {

    static Form<Part> partForm = Form.form(Part.class);

    public static Result index() {
        return ok(views.html.parts_index.render(Part.all(), partForm));
    }

    public static Result newPart() {
        Form<Part> filledForm = partForm.bindFromRequest();
        MultipartFormData body = request().body().asMultipartFormData();
        Logger.debug(filledForm.toString());
        String fileName = null;
        String contentType = null;
        String desc = "";
        FilePart description = body.getFile("description");

        if (description != null) {
            fileName = description.getFilename();
            contentType = description.getContentType(); 
            File file = description.getFile();

            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                //FileWriter fw = new FileWriter("/../../public/descriptions/" + fileName + ".txt", true);
                String s;
                while((s = br.readLine()) != null) {
                    desc.concat("\n" + s);
                    // fw.write(s);
                }
                fr.close();
                // fw.close();
            }
            catch(FileNotFoundException fnfe) {
                System.err.println(fnfe.getStackTrace());
            }
            catch(IOException ioe) {
                System.err.println(ioe.getStackTrace());
            }
        } else {
            flash("error", "Missing file");
            return redirect(routes.Parts.index());    
        }
        if(filledForm.hasErrors()) {
            return badRequest(
                views.html.parts_index.render(Part.all(), filledForm)
            );
        } else {
            Part part = filledForm.get();
            if(!part.phone.matches("^\\(\\d{3}\\)\\d{3}-\\d{4}|\\d{3}-\\d{3}-\\d{4}|\\d{10}$")) {
                return badRequest(
                    views.html.parts_index.render(Part.all(), filledForm)
                );
            }
            // part.setDesc(fileName);
            part.description = desc;

            // SimpleEmail email = new SimpleEmail();
            // email.setFrom(part.creator + "@AssetManager");
            // email.addTo(app.configuration().getString("ownerEmail"));
            // email.addTo(part.email);
            // email.setSubject("Part Added: " + part.vendor + " - " + part.label);
            // email.setMsg("A Part has been added to the Asset Manager:\n\n"
            //     + part.toString());
            // Mail.send(email);

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
