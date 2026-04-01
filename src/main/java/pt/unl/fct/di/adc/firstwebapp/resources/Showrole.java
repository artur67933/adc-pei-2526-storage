package pt.unl.fct.di.adc.firstwebapp.resources;

import com.google.cloud.datastore.Entity;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.adc.firstwebapp.util.Metodos;
import pt.unl.fct.di.adc.firstwebapp.util.Mystatus;
import pt.unl.fct.di.adc.firstwebapp.util.Roles;
import pt.unl.fct.di.adc.firstwebapp.util.Inputs.Sendinput;
import pt.unl.fct.di.adc.firstwebapp.util.Inputs.Username;

@Path("/showuserrole")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Showrole {
	public Showrole() {
	}
 
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response showrole(Sendinput data) {

		if(data == null || data.input==null || !(data.input instanceof Username)) 
			return Metodos.error(Mystatus.INVALID_INPUT); 
		Response r=Metodos.validtoken(data.token); 
		if(r!=null)
			return r;
		switch(Roles.getrole(data.token.role)) { 
		case ADMIN:case BOFFICER: 
			String username = ((Username)data.input).username; 
			Entity user = Metodos.getUserentity(username); 
			if(user==null) 
				return Metodos.error(Mystatus.USER_NOT_FOUND); 
			else 
				return Metodos.success(Metodos.getUser(username)); 
		default: 
			return Metodos.error(Mystatus.UNAUTHORIZED); 
			}

	}
}
