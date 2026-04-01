package pt.unl.fct.di.adc.firstwebapp.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.adc.firstwebapp.util.Metodos;
import pt.unl.fct.di.adc.firstwebapp.util.Mystatus;
import pt.unl.fct.di.adc.firstwebapp.util.Roles;
import pt.unl.fct.di.adc.firstwebapp.util.Inputs.Role;
import pt.unl.fct.di.adc.firstwebapp.util.Inputs.Sendinput;

@Path("/changeuserrole")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Changeuserrole {
	private static final String SUCCESS = "Role updated successfully";

	public Changeuserrole() {
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modaccount(Sendinput data) {
		if (data == null || data.input == null || !(data.input instanceof Role))
			return Metodos.error(Mystatus.INVALID_INPUT);
		Response r = Metodos.validtoken(data.token);
		if (r != null)
			return r;
		Role role = (Role) data.input;

		switch (Roles.getrole(data.token.role)) {
		case ADMIN:
			r = Metodos.edituserrole(role.username, Roles.getrole(role.role));
			if (r != null)
				return r;
			return Metodos.successmessage(SUCCESS);
		default:
			return Metodos.error(Mystatus.UNAUTHORIZED);
		}
	}
}
