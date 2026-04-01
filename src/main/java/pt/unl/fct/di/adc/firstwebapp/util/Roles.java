package pt.unl.fct.di.adc.firstwebapp.util;

public enum Roles {
	USER, ADMIN, BOFFICER;

	public static Roles getrole(String str) {
		try {
			return Roles.valueOf(str);
		} catch (Exception e) {
			return null;
		}
	}
}
