package pt.unl.fct.di.adc.firstwebapp.resources;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.adc.firstwebapp.util.AuthToken;
import pt.unl.fct.di.adc.firstwebapp.util.LoginData;
import pt.unl.fct.di.adc.firstwebapp.util.Metodos;
import pt.unl.fct.di.adc.firstwebapp.util.Mystatus;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {
	private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public LoginResource() {}

	public static class Sendinput {
		public LoginData input; 
		public Sendinput() { }}
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doLogin(Sendinput input) {
		if (input == null)return Metodos.error(Mystatus.INVALID_INPUT);
		LoginData data=input.input;
		if (data == null)return Metodos.error(Mystatus.INVALID_INPUT);
		Entity user = Metodos.getUserentity(data.username);
		if (user == null)return Metodos.error(Mystatus.USER_NOT_FOUND);
		else if (user.getString("user_pwd").equals(DigestUtils.sha512Hex(data.password))) {
			AuthToken at = new AuthToken(data.username,user.getString("user_role"));

			Transaction txn = datastore.newTransaction();
			Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(at.tokenId);
			Entity newtoken = Entity.newBuilder(tokenKey)
					.set("token_name", at.username)
					.set("token_creationData", at.issuedAt)
					.set("token_expirationData", at.expiresAt)
					.set("token_ID", at.tokenId)
					.set("token_role", at.role)
					.build();
			txn.put(newtoken);
			txn.commit();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("token", at.tojson());
			return Metodos.success(map);
			
		} else
			return Metodos.error(Mystatus.INVALID_CREDENTIALS);

	}

}