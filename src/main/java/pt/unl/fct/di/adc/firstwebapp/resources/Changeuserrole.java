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
import pt.unl.fct.di.adc.firstwebapp.util.Roles;

@Path("/changeuserrole")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Changeuserrole {
	private static final String SUCCESS = "Role updated successfully";

	public Changeuserrole() {}
	public class Role{
		public String username;
		public String role;
		public Role(Map<String, Object> map) {
			this.username = (String) map.get("username");
			this.role = (String) map.get("role");
		}
	}
	public class Sendinput {
		public AuthToken token;
		public Role input;
		@SuppressWarnings("unchecked")
		public Sendinput(Map<String, Object> map) {
			this.input = new Role((Map<String, Object>) map.get("input"));
			this.token = new AuthToken((Map<String, Object>) map.get("token"));
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
			if (r != null)
				return r;
			if (data.input == null)
				return Metodos.error(Mystatus.INVALID_INPUT);
			Role role = data.input;

			switch (Roles.getrole(data.token.role)) {
			case ADMIN:
				r = Metodos.edituserrole(role.username, role.role);
				if (r != null)
					return r;
				return Metodos.successmessage(SUCCESS);
			default:
				return Metodos.error(Mystatus.UNAUTHORIZED);
			}
		}catch(Exception e) {return Metodos.error(Mystatus.INVALID_INPUT);}
	}
}

