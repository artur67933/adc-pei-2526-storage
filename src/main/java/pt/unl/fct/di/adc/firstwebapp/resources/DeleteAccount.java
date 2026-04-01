package pt.unl.fct.di.adc.firstwebapp.resources;

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
import pt.unl.fct.di.adc.firstwebapp.util.Metodos;
import pt.unl.fct.di.adc.firstwebapp.util.Mystatus;
import pt.unl.fct.di.adc.firstwebapp.util.Roles;
import pt.unl.fct.di.adc.firstwebapp.util.Inputs.Sendinput;
import pt.unl.fct.di.adc.firstwebapp.util.Inputs.Username;

@Path("/deleteaccount")
public class DeleteAccount {
	private static final String SUCCESS = "Account deleted successfully";

	public DeleteAccount() {
	}
 
	private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteaccount(Sendinput data) {

		if (data == null || data.input == null || !(data.input instanceof Username))
			return Metodos.error(Mystatus.INVALID_INPUT);
		Response r = Metodos.validtoken(data.token);
		if (r != null)
			return r;
		if (Roles.ADMIN != Roles.getrole(data.token.role))
			return Metodos.error(Mystatus.UNAUTHORIZED);
		Transaction txn = datastore.newTransaction();
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(((Username) data.input).username);
		Entity user = txn.get(userKey);
		if (user == null)
			return Metodos.error(Mystatus.USER_NOT_FOUND);

		datastore.delete(userKey);
		user = txn.get(userKey);
		if (user != null)
			return Metodos.error(Mystatus.FORBIDDEN);
		else
			return Metodos.successmessage(SUCCESS);
	}
}
