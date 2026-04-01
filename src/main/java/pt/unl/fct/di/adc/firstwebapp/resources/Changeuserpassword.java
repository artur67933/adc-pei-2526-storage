package pt.unl.fct.di.adc.firstwebapp.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.adc.firstwebapp.util.Metodos;
import pt.unl.fct.di.adc.firstwebapp.util.Mystatus;
import pt.unl.fct.di.adc.firstwebapp.util.Inputs.Sendinput;
import pt.unl.fct.di.adc.firstwebapp.util.Inputs.pwd;

@Path("/changeuserpwd")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Changeuserpassword {
	private static final String SUCCESS = "Password changed successfully";

	public Changeuserpassword() {
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modaccount(Sendinput data) {
		if (data == null || data.input == null || !(data.input instanceof pwd))
			return Metodos.error(Mystatus.INVALID_INPUT);
		Response r = Metodos.validtoken(data.token);
		if (r != null)
			return r;
		pwd pwd = (pwd) data.input;
		if (!pwd.username.equals(data.token.username))
			return Metodos.error(Mystatus.UNAUTHORIZED);
		if (!pwd.oldPassword.equals(Metodos.getUserentity(pwd.username).getString("user_pwd")))
			return Metodos.error(Mystatus.INVALID_CREDENTIALS);
		r = Metodos.edituserpwd(pwd.username, pwd.newPassword);
		if (r != null)
			return r;
		return Metodos.successmessage(SUCCESS);
	}
}
