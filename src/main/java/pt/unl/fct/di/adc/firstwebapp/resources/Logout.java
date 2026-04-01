package pt.unl.fct.di.adc.firstwebapp.resources;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Transaction;

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

@Path("/logout")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Logout {
	private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private static final String SUCCESS = "Logout successful";

	public Logout() {} // Nothing to be done here

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logout(Sendinput data) {
		if (data == null || data.input == null || !(data.input instanceof Username))
			return Metodos.error(Mystatus.INVALID_INPUT);
		Response r = Metodos.validtoken(data.token);
		if (r != null)
			return r;
		String username = ((Username) data.input).username;
 
		switch (Roles.getrole(data.token.role)) {
		default:
			if (!data.token.username.equals(username))
				return Metodos.error(Mystatus.UNAUTHORIZED);
		case ADMIN:
			if (Metodos.getUserentity(username) != null)
				return Metodos.error(Mystatus.USER_NOT_FOUND);
			getvalidtokens(username);

		}
		return Metodos.successmessage(SUCCESS);
	}



	private void getvalidtokens(String user) {
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("Token").build();
		QueryResults<Entity> results = datastore.run(query);
		while (results.hasNext()) {
			Entity en = results.next();
			long expirationData = en.getLong("token_expirationData");
			String username = en.getString("token_name");
			Transaction txn = datastore.newTransaction();
			Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(en.getString("token_ID"));
			Entity newtoken;
			if (System.currentTimeMillis() >= expirationData && username.equals(user)) {
				newtoken = Entity.newBuilder(tokenKey)
						.set("token_name", username)
						.set("token_creationData", en.getLong("token_creationData"))
						.set("token_expirationData", System.currentTimeMillis()/1000)
						.set("token_ID", en.getString("token_ID"))
						.set("token_role", en.getString("token_role"))
						.build();
				txn.put(newtoken);
				txn.commit();
			}
		}
	}

}