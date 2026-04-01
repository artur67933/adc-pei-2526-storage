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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.adc.firstwebapp.util.Metodos;
import pt.unl.fct.di.adc.firstwebapp.util.Mystatus;
import pt.unl.fct.di.adc.firstwebapp.util.RegisterData;

@Path("/createaccount")
public class RegisterResource {

	private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public static class InputData {
		public RegisterData input;
	}

	public RegisterResource() {
	} // Default constructor, nothing to do

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(InputData input) {
		if (input == null)
			return Metodos.error(Mystatus.INVALID_INPUT);
		RegisterData data=input.input;

		if(!data.validRegistration()) return
				Metodos.error(Mystatus.INVALID_CREDENTIALS); 
		Transaction txn = datastore.newTransaction(); 
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username); 
		Entity user= txn.get(userKey);

		if(user != null) 
			return Metodos.error(Mystatus.USER_ALREADY_EXISTS); 
		else {
			user = Entity.newBuilder(userKey) 
					.set("user_name", data.username)
					.set("user_pwd", DigestUtils.sha512Hex(data.password))
					.set("user_phone",data.phone) 
					.set("user_address", data.address) 
					.set("user_role",data.role.toString()) 
					.build(); 
			txn.put(user); 
			txn.commit();
			Map<String,Object> map = new HashMap<String, Object>(); 
			map.put("username", data.username);
			map.put("role", data.role.toString()); 
			return Metodos.success(map); 
		}

	}
}