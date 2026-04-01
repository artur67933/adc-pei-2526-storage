package pt.unl.fct.di.adc.firstwebapp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthToken {

	public static final long EXPIRATION_TIME = 1000*60*60*2; // 2h
	
	public String username;
	public String tokenId;
	public String role;
	public Integer issuedAt;
	public Integer expiresAt;
	
	public AuthToken() { }
	
	public AuthToken(String username,String role) {
		this.username = username;
		this.tokenId = UUID.randomUUID().toString();
		this.role = role;
		this.issuedAt = (int) (System.currentTimeMillis()/1000);
		this.expiresAt = (int) (this.issuedAt + (EXPIRATION_TIME/1000));
	}
	
	public AuthToken(Map<String, Object> map) {
		this.username = (String)map.get("username");
		this.tokenId = (String)map.get("tokenId");
		this.issuedAt = (Integer)map.get("issuedAt");
		this.expiresAt = (Integer)map.get("expiresAt");
		this.role = (String)map.get("role");
	}
	
	public AuthToken(String username,String tokenId,String role,int issuedAt,int expiresAt) {
		this.username = username;
		this.tokenId = tokenId;
		this.role= role;
		this.issuedAt = issuedAt;
		this.expiresAt = expiresAt;
	}

	public boolean nonull() {
		return (username!=null)&&(tokenId!=null)&&(role!=null);
	}
	
	public Map<String, Object> tojson(){
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("username", username);
		map.put("tokenId", tokenId);
		map.put("role", role);
		map.put("issuedAt", issuedAt);
		map.put("expiresAt", expiresAt);
		return map;
	}
	
	
}
