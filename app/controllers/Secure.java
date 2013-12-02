package controllers;

import play.*;
import play.mvc.Controller;

public class Secure extends Controller {
	
	public static boolean check(String profile) {
		if(profile.equals("admin"))
		// return session.get("username").equals("admin");
		return true;
		return false;
	}

	public static boolean authentify(String username, String password) {  
		return Play.application().configuration().getString("application.admin").equals(username) &&
			Play.application().configuration().getString("application.adminpwd").equals(password);  
	}  
}