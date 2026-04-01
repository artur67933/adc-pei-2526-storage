package pt.unl.fct.di.adc.firstwebapp.util.Inputs;

public class Mod{

	public String username;
	public Atributes attributes;
	public Mod() { }

	public Mod(String username,String phone,String address) {
		this.username = username;
		this.attributes=new Atributes(phone,address);
	}
	public class Atributes{
		public String phone;
		public String address;
		
		public Atributes() { }

		public Atributes(String phone,String address) {
			this.phone = phone;
			this.address = address;
		}
	}	
}