package pt.unl.fct.di.adc.firstwebapp.resources;

import java.util.Map;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.adc.firstwebapp.util.AuthToken;
import pt.unl.fct.di.adc.firstwebapp.util.Metodos;
import pt.unl.fct.di.adc.firstwebapp.util.Mystatus;
import pt.unl.fct.di.adc.firstwebapp.util.Roles;

@Path("/deleteaccount")
public class DeleteAccount {
	private static final String SUCCESS = "Account deleted successfully";

	public DeleteAccount() {}
	public class Username{
		public Username(Map<String, Object> map) {
			this.username = (String) map.get("username");
		}
		public String username;}
	public class Sendinput {
		public AuthToken token;
		public Username input;
		@SuppressWarnings("unchecked")
		public Sendinput(Map<String, Object> map) {
			this.input = new Username((Map<String, Object>) map.get("input"));
			this.token = new AuthToken((Map<String, Object>) map.get("token"));
		}
	}

	private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteaccount(Object input) {
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
			if (Roles.ADMIN != Roles.getrole(data.token.role))
				return Metodos.error(Mystatus.UNAUTHORIZED);
			String username=data.input.username;
			Transaction txn = datastore.newTransaction();
			Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
			Entity user = txn.get(userKey);
			if (user == null)
				return Metodos.error(Mystatus.USER_NOT_FOUND);
			Metodos.deletetokens(username);
			datastore.delete(userKey);
			user = txn.get(userKey);
			if (user != null)
				return Metodos.error(Mystatus.FORBIDDEN);
			else
				return Metodos.successmessage(SUCCESS);
		}catch(Exception e) {return Metodos.error(Mystatus.INVALID_INPUT);}
	}


}
