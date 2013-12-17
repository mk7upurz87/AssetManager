package controllers;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import play.mvc.*;
import play.data.*;
import models.*;

public class Bids extends Controller {

    static Form<Bid> bidForm = Form.form(Bid.class);
    static Long currPartId;

    public static Result index() {
        return ok(views.html.bids_index.render(Bid.all(), bidForm, User.all()));
    }

    public static Result newBid(Long partId) {
        Part part = Part.getById(partId);
        return ok(views.html.part_detail.render(part, bidForm, User.all(), null));
    }

    public static Result createBid(Long partId) {
        Form<Bid> filledForm = bidForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            System.err.println(filledForm.toString());
            return badRequest(
                views.html.part_detail.render(null, filledForm, User.all(), "Sorry, there was a problem with your request.  If this problem persists, please contact MedTech.")
            );
        } else {
            String username = session().get("username");
            User user = User.getByUsername(username);
            
            Bid bid = filledForm.get();
            bid.user = user;
            bid.part = Part.getById(partId);
            if(bid.user.hasBidOnPart(bid.part.id)) {
                return badRequest(
                    views.html.part_detail.render(null, filledForm, User.all(), "You have already bid on: " + bid.part.label + ".  Please check your current bids.")
                );
            }
            Bid.create(bid);

            try {
                String host = "smtp.gmail.com";
                String username = "MedTechAM@gmail.com";
                String password = "Somethinggreat7";
                InternetAddress[] addresses = {
                                new InternetAddress("f.pecora@p3systemsinc.com"),
                                new InternetAddress(bid.email)
                                new InternetAddress("dgeorge@p3systemsinc.com")
                };
                Properties props = new Properties();

                // set any needed mail.smtps.* properties here                
                Session session = Session.getInstance(props);
                Multipart mp = new MimeMultipart();
                MimeMessage message = new MimeMessage(session);
                    message.setSubject("Bid Added: " + bid.part.vendor + " - " + bid.part.label);
                    message.setRecipients(Message.RecipientType.TO, addresses);
                    
                // create the body of the email                
                MimeBodyPart htmlPart = new MimeBodyPart();
                String bodyContent = "";
                
                // create the attachment of the email                
                if(bid.part.attachment != null) {
                        MimeBodyPart attach = new MimeBodyPart();
                        FileDataSource source = new FileDataSource(part.attachment);
                        attach.setDataHandler(new DataHandler(source));
                        attach.setFileName(source.getName());
                        attach.setDisposition(Part.ATTACHMENT);
                        mp.addBodyPart(attach);
                        
                        bodyContent = "A Bid has been placed using the Asset Manager:\n\n"
		                + bid.part.toString()
		                + "<p>Amount: $" + bid.value + "</p>"
		                + "<p>Comment: " + bid.comment + "</p>"
		                + "<p>See attached for more information</p>";
		                
		                // "<h2>A Part has been added to the Asset Manager:</h2>"
                  //                  + part.toString()
                  //                  + "<br />"
                  //                  + "See attached File for details...";
                } else {
                        bodyContent = "<h2>A Part has been added to the Asset Manager:</h2>"
                                    + part.toString();
                }

                htmlPart.setContent(bodyContent, "text/html");
                mp.addBodyPart(htmlPart);
                    message.setContent(mp);
                    
                                // set the message content here
                Transport t = session.getTransport("smtps");
                try {
                	t.connect(host, uname, password);
                	t.sendMessage(message, message.getAllRecipients());
                } finally {
                	t.close();
                }  	
	    }
            catch (MessagingException me) {
		me.printStackTrace();
	    }
            user.bids.add(bid);

            if(!("admin").equals(username)) {
                return redirect(routes.Users.viewUser(user.id));
            }
            return redirect(routes.Bids.index());
        }
    }

    public static Result acceptBid(Long id) {
        //TODO send an email out to the bidder
        Bid.delete(id);
        return redirect(routes.Bids.index());
    }

    public static Result deleteBid(long id) {
        Bid.delete(id);
        return redirect(routes.Bids.index());
    }
}
