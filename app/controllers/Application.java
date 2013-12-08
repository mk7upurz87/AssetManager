package controllers;

import play.mvc.*;
import play.data.*;

import models.*;

public class Application extends Controller {

    static Form<Login> loginForm = Form.form(Login.class);
    static Form<User> userForm = Form.form(User.class);

    public static Result index() {
        return ok(views.html.index.render(loginForm));
    }

    public static Result app_index() {
        return Parts.index();
    }

    public static boolean checkLoggedIn() {
    	String currUser = session().get("username");
    	for(User user : User.all()) {
    		if(user.username.equals(currUser)) {
    			return true;
    		}
    	}
    	return false;
    }
}