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

@Path("/modaccount")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Modifyaccount {
	private static final String SUCCESS = "Updated successfully";

	public Modifyaccount() {}

	public class Mod{
		public String username;
		public Atributes attributes;
		@SuppressWarnings("unchecked")
		public Mod(Map<String, Object> map) {
			this.username = (String) map.get("username");
			this.attributes = new Atributes((Map<String, Object>) map.get("attributes"));
		}
	}
	public class Atributes{
		public String phone;
		public String address;
		public Atributes(Map<String, Object> map) {
			this.phone = (String) map.get("phone");
			this.address = (String) map.get("address");
		}
	}	
	public class Sendinput {
		public AuthToken token;
		public Mod input;
		@SuppressWarnings("unchecked")
		public Sendinput(Map<String, Object> map) {
			this.input = new Mod((Map<String, Object>) map.get("input"));
			this.token = new AuthToken((Map<String, Object>) map.get("token"));
		}
	}
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modaccount(Object input) {
		if (input == null)
			return Metodos.error(Mystatus.INVALID_INPUT);
		try {
			@SuppressWarnings("unchecked")
			Sendinput data=new Sendinput((Map<String, Object>) input);
			Response r = Metodos.validtoken(data.token);
			if (r != null)return r;
			if (data.input == null)
				return Metodos.error(Mystatus.INVALID_INPUT);
			Mod mod = data.input;
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
		}catch(Exception e) {return Metodos.error(Mystatus.INVALID_INPUT);}
	}


	private Response change(Mod mod) {
		Response r = Metodos.edituserphoneaddress(mod.username, mod.attributes.phone, mod.attributes.address);
		if (r != null)
			return r;
		return Metodos.successmessage(SUCCESS);
	}

}
