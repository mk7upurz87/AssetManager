package controllers;

import play.mvc.*;
import play.api.Logger;
import play.data.*;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import models.*;

public class Parts extends Controller {

    static Form<models.Part> partForm = Form.form(models.Part.class);

    public static Result index() {
        return ok(views.html.parts_index.render(models.Part.all(), partForm));
    }

    public static Result newPart() {
        Form<models.Part> filledForm = partForm.bindFromRequest();
        play.mvc.Http.MultipartFormData body = request().body().asMultipartFormData();
        String fileName = null;
        play.mvc.Http.MultipartFormData.FilePart description = body.getFile("description");

        if (description != null) {
            fileName = description.getFilename();
            
            if (fileName.matches("[_a-zA-Z0-9\\-\\.]+")) {
	            File file = description.getFile();
	            
	            try {
	            	//Open the byte stream to read in the bytes
	            	InputStream in = new FileInputStream(file);
            	    BufferedReader br =
            	      new BufferedReader(new InputStreamReader(in));
            	    
            	    //If the file is not on the server, create one
            	    if(!file.exists()) {
            	    	Logger.apply("file did not exist in system");
            	    	file = new File("emptyDesc.txt");
            	    }
            	    
            	    //Open the the byte stream to output the bytes
	                OutputStream out = new FileOutputStream(fileName, true);
	                String s;
	                
	                while((s = br.readLine()) != null) {
	                    out.write(s.getBytes());
	                }
	                in.close();
	                out.close();
	            }
	            catch(FileNotFoundException fnfe) {
	                System.err.println(fnfe.getStackTrace());
	            }
	            catch(IOException ioe) {
	                System.err.println(ioe.getStackTrace());
	            }
            }
        } else {
            flash("error", "Missing file");
            return redirect(routes.Parts.index());    
        }
        if(filledForm.hasErrors()) {
            return badRequest(
                views.html.parts_index.render(models.Part.all(), filledForm)
            );
        } else {
            models.Part part = filledForm.get();
            
            try {
                String host = "smtp.gmail.com";
                String username = "MedTechAM@gmail.com";
                String password = "Somethinggreat7";
                InternetAddress[] addresses = {new InternetAddress("f.pecora@p3systemsinc.com"),
                    new InternetAddress(part.email),
                    new InternetAddress("dgeorge@p3systemsinc.com")};
                Properties props = new Properties();
                
                // set any needed mail.smtps.* properties here
                Session session = Session.getInstance(props);
                MimeMessage message = new MimeMessage(session);
	            message.setSubject("Part Added: " + part.vendor + " - " + part.label);
	            message.setContent("A Part has been added to the Asset Manager:\n\n"
		                + part.toString(), "text/plain");
	            message.setRecipients(Message.RecipientType.TO, addresses);
                
                // set the message content here
                Transport t = session.getTransport("smtps");
                try {
                	t.connect(host, username, password);
                	t.sendMessage(message, message.getAllRecipients());
                } finally {
                	t.close();
                }  	
			}
            catch (MessagingException me) {
				me.printStackTrace();
			}

            models.Part.create(part);
            return redirect(routes.Parts.index());
        }
    }

    public static Result deletePart(long id) {
        models.Part.delete(id);
        List<Bid> bids = Bid.all();
        for(Bid bid : bids) {
            if(bid.part.id == id) {
                bid.delete();
            }
        }
        return redirect(routes.Parts.index());
    }
}
