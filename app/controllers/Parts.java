package controllers;

import play.Play;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.*;
import play.data.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Part;
import javax.mail.internet.*;

import models.*;

public class Parts extends Controller {

    static Form<models.Part> partForm = Form.form(models.Part.class);

    public static Result index() {
        return ok(views.html.parts_index.render(models.Part.all(), partForm));
    }

	public static Result newPart() {
        Form<models.Part> filledForm = partForm.bindFromRequest();
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart attachment = body.getFile("attachment");
        
        String fileName = null;
        String newFilePath = null;
        File file = null;
        
        if (attachment != null) {
            fileName = attachment.getFilename().replace(" ", "_");
            
            if (fileName.matches("[_a-zA-Z0-9\\-\\.]+")) {
	            file = attachment.getFile();
	            
	            try {
            	    String applicationRoot = Play.application().path().getPath();
        	    	newFilePath = applicationRoot + "\\public\\attachments\\" + fileName;
        	    	File newFile = new File(newFilePath);
    	        	Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    	        	file = newFile;
	            }
	            catch(FileNotFoundException fnfe) {
	            	System.err.println("File: " + file.getName() + " \n" + fnfe.getStackTrace());
	            }	            
	            catch(IOException ioe) {
	                System.err.println(ioe.getStackTrace());
	            }
            }
        }
        if(filledForm.hasErrors()) {
            return badRequest(
                views.html.parts_index.render(models.Part.all(), filledForm)
            );
        } else {
            models.Part part = filledForm.get();
            
            if(newFilePath != null) {
            	part.attachment = file;
            	part.attachmentName = file.getName();
            	part.save();
            }
            try {
                String host = "smtp.gmail.com";
                String username = "MedTechAM@gmail.com";
                String password = "Somethinggreat7";
                InternetAddress[] addresses = {
                		new InternetAddress("f.pecora@p3systemsinc.com"),
                		new InternetAddress(part.email)
                		new InternetAddress("dgeorge@p3systemsinc.com")
                };
                Properties props = new Properties();

                // set any needed mail.smtps.* properties here                
                Session session = Session.getInstance(props);
                Multipart mp = new MimeMultipart();
                MimeMessage message = new MimeMessage(session);
	            message.setSubject("Part Added: " + part.vendor + " - " + part.label);
	            message.setRecipients(Message.RecipientType.TO, addresses);
	            
                // create the body of the email                
                MimeBodyPart htmlPart = new MimeBodyPart();
                String bodyContent = "";
                
                // create the attachment of the email                
                if(part.attachment != null) {
	                MimeBodyPart attach = new MimeBodyPart();
	                FileDataSource source = new FileDataSource(part.attachment);
	                attach.setDataHandler(new DataHandler(source));
	                attach.setFileName(source.getName());
	                attach.setDisposition(Part.ATTACHMENT);
	                mp.addBodyPart(attach);
	                
	                bodyContent = "<h2>A Part has been added to the Asset Manager:</h2>"
    		                + part.toString()
    		                + "<br />"
    		                + "See attached File for details...";
                } else {
                	bodyContent = "<h2>A Part has been added to the Asset Manager:</h2>"
    		                + part.toString();
                }

                htmlPart.setContent(bodyContent, "text/html");
                mp.addBodyPart(htmlPart);
            	message.setContent(mp);
	            
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
