package pt.unl.fct.di.adc.firstwebapp.resources;

import java.util.Map;

import com.google.cloud.datastore.Entity;

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

@Path("/showuserrole")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Showrole {
	public Showrole() {}
	public class Username{
		public String username;
		public Username(Map<String, Object> map) {
			this.username = (String) map.get("username");
		}
	}
	public class Sendinput {
		public AuthToken token;
		public Username input;
		@SuppressWarnings("unchecked")
		public Sendinput(Map<String, Object> map) {
			this.input = new Username((Map<String, Object>) map.get("input"));
			this.token = new AuthToken((Map<String, Object>) map.get("token"));
		}
	}
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response showrole(Object input) {
		if (input == null)
			return Metodos.error(Mystatus.INVALID_INPUT);
		try {
			@SuppressWarnings("unchecked")
			Sendinput data=new Sendinput((Map<String, Object>) input);
			Response r=Metodos.validtoken(data.token); 
			if(r!=null)	return r;
			if(data.input==null) 
				return Metodos.error(Mystatus.INVALID_INPUT); 
			switch(Roles.getrole(data.token.role)) { 
			case ADMIN:case BOFFICER:
					String username = data.input.username; 
					Entity user = Metodos.getUserentity(username); 
					if(user==null) 
						return Metodos.error(Mystatus.USER_NOT_FOUND); 
					else 
						return Metodos.success(Metodos.getUser(username));
			default: 
				return Metodos.error(Mystatus.UNAUTHORIZED); 
			}
		}catch(Exception e) {return Metodos.error(Mystatus.INVALID_INPUT);}
	}
}
