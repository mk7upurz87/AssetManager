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
        currPartId = partId;
        return ok(views.html.bids_new.render(bidForm, User.all()));
    }

    public static Result createBid() {
        Form<Bid> filledForm = bidForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            System.err.println(filledForm.toString());
            return badRequest(
                views.html.bids_new.render(filledForm, User.all())
            );
        } else {
            String username = session().get("username");
            User user = User.getByUsername(username);
            
            Bid bid = filledForm.get();
            bid.user = user;
            bid.part = new Part(new Long(1),"John Smith","2nd","mk7upurz87@gmail.com",
                "123-456-7890","MRI","Block Image",new Long(2),"part.txt");
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
