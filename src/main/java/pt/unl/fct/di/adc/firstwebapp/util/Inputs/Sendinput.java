package pt.unl.fct.di.adc.firstwebapp.util.Inputs;

import pt.unl.fct.di.adc.firstwebapp.util.AuthToken;

public class Sendinput {
	public AuthToken token;
	public Object input; 

	public Sendinput() { }
	
	public Sendinput(AuthToken token,Object input) {
		this.token = token;
		this.input = input;
	}
}
