package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import views.html.*;
import models.*;

import java.util.Map;

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

            user.bids.add(bid);

            if(!("admin").equals(username)) {
                return redirect(routes.Users.viewUser(user.id));
            }
            return redirect(routes.Bids.index());
        }
    }

    public static Result deleteBid(long id) {
        Bid.delete(id);
        return redirect(routes.Bids.index());
    }
}
