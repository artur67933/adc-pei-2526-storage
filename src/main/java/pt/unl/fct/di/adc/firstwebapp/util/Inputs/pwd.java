package pt.unl.fct.di.adc.firstwebapp.util.Inputs;

public class pwd{
	public String username;
	public String oldPassword;
	public String newPassword;
	public pwd() { }

	public pwd(String username,String oldPassword,String newPassword) {
		this.username = username;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
}
