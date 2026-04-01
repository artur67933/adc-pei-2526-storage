package pt.unl.fct.di.adc.firstwebapp.util;

import java.util.Map;

public class RegisterData {
	
	public String username;
	public String password;
	public String confirmation;
	public String phone;
	public String address;
	public String role;
	
	public RegisterData() {
		
	}
	
	public RegisterData(String username, String password, String confirmation, String phone, String address,String role) {
		this.username = username;
		this.password = password;
		this.confirmation = confirmation;
		this.phone = phone;
		this.address = address;
		this.role = role;
	}
	
	public RegisterData(Map<String, Object> map) {
		this.username = (String)map.get("username");
		this.password = (String)map.get("password");
		this.confirmation = (String)map.get("confirmation");
		this.phone = (String)map.get("phone");
		this.address = (String)map.get("address");
		this.role = (String)map.get("role");
	}

	private boolean nonEmptyOrBlankField(String field) {
		return field != null && !field.isBlank();
	}
	
	
	public boolean validRegistration() {
		return nonEmptyOrBlankField(username) &&
			   username.contains("@") &&
			   nonEmptyOrBlankField(password) &&
			   nonEmptyOrBlankField(phone) &&
			   validphone() &&
			   nonEmptyOrBlankField(address) &&
			   role != null && Roles.getrole(role)!=null &&
			   password.equals(confirmation);
	}
	
	public boolean validphone() {
		int n=0;
		if(phone.charAt(n)=='+')n++;
		for(;n<phone.length();n++)
			switch(phone.charAt(n)) {
			case '0':case '1':case '2':
			case '3':case '4':case '5':
			case '6':case '7':case '8':
			case '9':break;
			default:return false;
			}
		return true;
	}
	

	
}