package pt.unl.fct.di.adc.firstwebapp.resources;

import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.adc.firstwebapp.util.AuthToken;
import pt.unl.fct.di.adc.firstwebapp.util.Metodos;
import pt.unl.fct.di.adc.firstwebapp.util.Mystatus;

@Path("/changeuserpwd")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Changeuserpassword {
	private static final String SUCCESS = "Password changed successfully";	
	public Changeuserpassword() {}

	public class Sendinput {
		public AuthToken token;
		public pwd input;
		@SuppressWarnings("unchecked")
		public Sendinput(Map<String, Object> map) {
			this.input = new pwd((Map<String, Object>) map.get("input"));
			this.token = new AuthToken((Map<String, Object>) map.get("token"));
		}
	}
	public class pwd{
		public String username;
		public String oldPassword;
		public String newPassword;
		public pwd(Map<String, Object> map) {
			this.username = (String) map.get("username");
			this.oldPassword = (String) map.get("oldPassword");
			this.newPassword = (String) map.get("newPassword");
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modaccount(Object input) {
		if (input == null)
			return Metodos.error(Mystatus.INVALID_INPUT);
		try {
			@SuppressWarnings("unchecked")
			Sendinput data=new Sendinput((Map<String, Object>) input);
			Response r = Metodos.validtoken(data.token);
			if (r != null) return r;
			if (data.input == null)
				return Metodos.error(Mystatus.INVALID_INPUT);
			pwd pwd = data.input;
			if (!pwd.username.equals(data.token.username))
				return Metodos.error(Mystatus.UNAUTHORIZED);
			if (!pwd.oldPassword.equals(Metodos.getUserentity(pwd.username).getString("user_pwd")))
				return Metodos.error(Mystatus.INVALID_CREDENTIALS);
			r = Metodos.edituserpwd(pwd.username, pwd.newPassword);
			if (r != null)
				return r;
			return Metodos.successmessage(SUCCESS);
		}catch(Exception e) {return Metodos.error(Mystatus.INVALID_INPUT);}
	}
}
