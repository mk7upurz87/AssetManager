package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.validation.Constraints.*;

import views.html.*;
import models.*;

public class Login extends Controller {

    static Form<Login> form = Form.form(Login.class);

    @Required
    public String password;

    @Required
    public String username;

    public static Result login() {
    	return ok(views.html.index.render(form));
    }

    public static Result logout() {
    	session().clear();
    	return redirect(
    		routes.Application.index()
    	);
    }

	public static Result authenticate() {
	    Form<Login> loginForm = form.bindFromRequest();
	    if (loginForm.hasErrors()) {
	    	Logger.debug("there are errors...");
	        return badRequest(index.render(loginForm));
	    } else {
	        session().clear();
	        session("username", loginForm.get().username);
	        return redirect(
	            routes.Application.app_index()
	        );
	    }
    }

    public String validate() {
	    if (User.authenticate(username, password) == null) {
	    	return "Invalid user or password";
	    }
    	return null;
	}
}