package controllers;

import java.io.*;

import play.*;
import play.mvc.*;
import play.data.*;
import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.*;

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
        FilePart description = body.getFile("description");
        if (description != null) {
            fileName = description.getFilename();
            contentType = description.getContentType(); 
            File file = description.getFile();

            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                FileWriter fw = new FileWriter("/../../public/descriptions/" + fileName + ".txt", true);
                String s;
                while((s = br.readLine()) != null) {
                    fw.write(s);
                }
                fr.close();
                fw.close();
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
            Part.create(filledForm.get(), fileName);
            return redirect(routes.Parts.index());
        }
    }

    public static Result deletePart(long id) {
        Part.delete(id);
        return redirect(routes.Parts.index());
    }
}
