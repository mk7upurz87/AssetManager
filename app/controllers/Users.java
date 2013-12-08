package controllers;

import play.mvc.*;
import play.data.*;

import java.util.*;

import models.*;

public class Users extends Controller {

    static Form<User> userForm = Form.form(User.class);

    public static Result index() {
        return ok(views.html.users_index.render(User.all(), userForm));
    }

    public static Result viewUser(long id) {
        User user = User.find.byId(id);
        return ok(views.html.users_view.render(user));
    }

    public static Result newUser() {
        Form<User> filledForm = userForm.bindFromRequest();
        if(filledForm.hasErrors()) {
            return badRequest(
                views.html.users_index.render(User.all(), filledForm)
            );
        } else {
            User.create(filledForm.get());
            return redirect(routes.Users.index());
        }
    }

    public static Result deleteUser(long id) {
        User.delete(id);
        List<Bid> bids = Bid.all();
        for(Bid bid : bids) {
            if(bid.user.id == id) {
                bid.delete();
            }
        }
        return redirect(routes.Users.index());
    }
}
