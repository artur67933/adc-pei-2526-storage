package pt.unl.fct.di.adc.firstwebapp.resources;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

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

@Path("/showauthsessions")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Showsessions {
	private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	public Showsessions() {} // Nothing to be done here
	public class Sendinput {
		public AuthToken token;
		public Object input;
		@SuppressWarnings("unchecked")
		public Sendinput(Map<String, Object> map) {
			this.token = new AuthToken((Map<String, Object>) map.get("token"));
		}
	}
	@POST 
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response showusers(Object input) {
		if (input == null)
			return Metodos.error(Mystatus.INVALID_INPUT);
		try {
			@SuppressWarnings("unchecked")
			Sendinput data=new Sendinput((Map<String, Object>) input);
			Response r=Metodos.validtoken(data.token); 
			if(r!=null)	return r;
			switch(Roles.getrole(data.token.role)) { 
			case ADMIN: 
				Query<Entity> query = Query.newEntityQueryBuilder().setKind("Token").build();
				QueryResults<Entity> results = datastore.run(query);
				List<Object> tokens=new LinkedList<Object>(); 
				while (results.hasNext()) {
					Entity en=results.next();
					long expirationData=en.getLong("token_expirationData");
					if((System.currentTimeMillis()/1000)>=expirationData) { 
						Map<String, Object> map = new HashMap<String, Object>(); 
						map.put("tokenId",en.getString("token_ID")); 
						map.put("username",en.getString("token_name")); 
						map.put("role",en.getString("token_role")); 
						map.put("expiresAt",expirationData); 
						tokens.add(map); 
					} 
				}
				Map<String, Object> out=new HashMap<String, Object>(); 
				out.put("sessions", tokens); 
				return Metodos.success(out); 
			default: 
				return Metodos.error(Mystatus.UNAUTHORIZED);
			}
		}catch(Exception e) {return Metodos.error(Mystatus.INVALID_INPUT);}

	}
}
