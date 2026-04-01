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

@Path("/showusers")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ShowUsers {

	private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	public ShowUsers() {
	} 
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
			if(r!=null)
				return r;
			switch(Roles.getrole(data.token.role)) { 
			case ADMIN:case BOFFICER: 
				Query<Entity> query = Query.newEntityQueryBuilder().setKind("Token").build();
				QueryResults<Entity> results = datastore.run(query);
				List<Object> users=new LinkedList<Object>();

				while (results.hasNext()) {
					Entity en = results.next();
					Map<String,Object> map = new HashMap<String, Object>(); 
					map.put("username", en.getString("user_name"));
					map.put("role", en.getString("user_role")); 
					users.add(map);
				}
				Map<String, Object> map = new HashMap<String, Object>(); 
				map.put("users", users);
				return Metodos.success(map); 
			default: 
				return Metodos.error(Mystatus.UNAUTHORIZED); 
			}
		}catch(Exception e) {return Metodos.error(Mystatus.INVALID_INPUT);}
	}
}
