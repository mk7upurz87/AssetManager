package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import views.html.*;
import models.*;

import java.util.Map;

public class Bids extends Controller {

    static Form<Bid> bidForm = Form.form(Bid.class);

    public static Result index() {
        return ok(views.html.bids_index.render(Bid.all(), bidForm, User.all()));
    }

    public static Result newBid() {
        return ok(views.html.bids_new.render(Part.all(), bidForm, User.all()));
    }

    public static Result createBid() {
        Form<Bid> filledForm = bidForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            System.err.println(filledForm.toString());
            return badRequest(
                views.html.bids_new.render(Part.all(), filledForm, User.all())
            );
        } else {
            String username = session().get("username");
            User user = User.getByUsername(username);
            
            Bid bid = filledForm.get();
            bid.user = user;
            
            Bid.create(bid);
            
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
