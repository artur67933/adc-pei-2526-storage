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
import pt.unl.fct.di.adc.firstwebapp.util.Inputs.Mod;
import pt.unl.fct.di.adc.firstwebapp.util.Inputs.Sendinput;

@Path("/modaccount")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Modifyaccount {
	private static final String SUCCESS = "Updated successfully";

	public Modifyaccount() {
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modaccount(Sendinput data) {

		if (data == null || data.input == null || !(data.input instanceof Mod))
			return Metodos.error(Mystatus.INVALID_INPUT);
		Response r = Metodos.validtoken(data.token);
		if (r != null)
			return r;
		Mod mod = (Mod) data.input;
 
		switch (Roles.getrole(data.token.role)) {
		case ADMIN:
			return change(mod);
		case BOFFICER:
			Roles role = Metodos.getUserPose(mod.username);
			if (role == null)
				return Metodos.error(Mystatus.USER_NOT_FOUND);
			else if (role == Roles.USER)
				return change(mod);
		default:
			if (mod.username.equals(data.token.username))
				return change(mod);
			else
				return Metodos.error(Mystatus.UNAUTHORIZED);
		}
	}

	private Response change(Mod mod) {
		Response r = Metodos.edituserphoneaddress(mod.username, mod.attributes.phone, mod.attributes.address);
		if (r != null)
			return r;
		return Metodos.successmessage(SUCCESS);
	}

}
