package pt.unl.fct.di.adc.firstwebapp.util;

import java.util.HashMap;
import java.util.Map;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;
import com.google.gson.Gson;

import jakarta.ws.rs.core.Response;

public class Metodos {

	private final static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public static Roles getUserPose(String username) {
		Entity user = getUserentity(username);
		if (user == null)
			return null;
		else
			return Roles.getrole(user.getValue("user_role").toString());
	}

	public static Entity getUserentity(String username) {
		Transaction txn = datastore.newTransaction();
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username); 
		Entity user = txn.get(userKey);
		return user;
	}

	public static String getUser(String username) {
		Roles role=getUserPose(username); 
		if(role==null) 
			return null; 
		Map<String,Object> map = new HashMap<String, Object>(); 
		map.put("username", username);
		map.put("role", role); 
		return tojson(map);
	}

	private static Response edituser(String username, String pwd, String phone, String address, Roles role) {
		Transaction txn = datastore.newTransaction();
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(username);
		Entity user = txn.get(userKey), newuser;
		if (user == null)
			return Metodos.error(Mystatus.USER_NOT_FOUND);
		newuser = Entity.newBuilder(userKey).set("user_name", username)
				.set("user_pwd", (pwd != null) ? pwd : user.getString("user_pwd"))
				.set("user_phone", (phone != null) ? phone : user.getString("user_phone"))
				.set("user_address", (address != null) ? address : user.getString("user_address"))
				.set("user_role", (role != null) ? role.toString() : user.getString("user_role")).build();
		txn.put(newuser);
		txn.commit();
		user = txn.get(userKey);
		if (phone != null && !user.getString("user_pwd").equals(pwd))
			return Metodos.error(Mystatus.FORBIDDEN);
		if (phone != null && !user.getString("user_phone").equals(phone))
			return Metodos.error(Mystatus.FORBIDDEN);
		if (address != null && !user.getString("user_address").equals(address))
			return Metodos.error(Mystatus.FORBIDDEN);
		if (phone != null && !user.getString("user_role").equals(role.toString()))
			return Metodos.error(Mystatus.FORBIDDEN);
		return null;
	}

	public static Response edituserpwd(String username, String pwd) {
		return edituser(username, pwd, null, null, null);
	}

	public static Response edituserphoneaddress(String username, String phone, String address) {
		return edituser(username, null, phone, address, null);
	}

	public static Response edituserrole(String username, Roles role) {
		return edituser(username, null, null, null, role);
	}

	public static Response success(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", obj);
		map.put("status", "success");
		return end(tojson(map));
	}
	
	public static String tojson(Map<String, Object> obj) {
		return new Gson().toJson(obj);
	}

	public static Response validtoken(AuthToken token) {
		if (token==null || !token.nonull())
			return Metodos.error(Mystatus.INVALID_INPUT);
		Transaction txn = datastore.newTransaction();
		Key tokenKey = datastore.newKeyFactory().setKind("Token").newKey(token.tokenId);
		Entity tokenent = txn.get(tokenKey);
		if (tokenent == null)
			return error(Mystatus.INVALID_TOKEN);
		boolean b = true;
		b &= (tokenent.getString("token_name").equals(token.username));
		b &= (tokenent.getString("token_role").equals(token.role.toString()));
		b &= (tokenent.getLong("token_creationData") == token.issuedAt);
		b &= (tokenent.getLong("token_expirationData") == token.expiresAt);
		if (!b)
			return error(Mystatus.INVALID_TOKEN);
		else if (System.currentTimeMillis()/1000 > token.expiresAt)
			return error(Mystatus.TOKEN_EXPIRED);
		else
			return null;
	}

	public static Response error(Mystatus s) {
		return end(s.tojson());
	}

	public static Response end(String json) {
		return Response.ok().entity(json).build();
	}

	public static Response successmessage(String message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		return success(map);
	}
	


}
