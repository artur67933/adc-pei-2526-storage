package pt.unl.fct.di.adc.firstwebapp.util;

import java.util.HashMap;
import java.util.Map;
import jakarta.ws.rs.core.Response.Status.Family;
import jakarta.ws.rs.core.Response.StatusType;

public enum Mystatus implements StatusType {
	INVALID_CREDENTIALS(9900, "The username-password pair is not valid"),
	USER_ALREADY_EXISTS(9901, "Error in creating an account because the username already exists"),
	USER_NOT_FOUND(9902, "The username referred in the operation doesn’t exist in registered accounts"),
	INVALID_TOKEN(9903, "The operation is called with an invalid token (wrong format for example)"),
	TOKEN_EXPIRED(9904, "The operation is called with a token that is expired"),
	UNAUTHORIZED(9905, "The operation is not allowed for the user role"),
	INVALID_INPUT(9906, "The call is using input data not following the correct specification"),
	FORBIDDEN(9907, "The operation generated a forbidden error by other reason");

	private final int code;
	private final String reason;
	private final Family family;

	Mystatus(final int statusCode, final String reasonPhrase) {
		this.code = statusCode;
		this.reason = reasonPhrase;
		this.family = Family.familyOf(statusCode);
	}

	@Override
	public int getStatusCode() {
		return code;
	}

	@Override
	public String getReasonPhrase() {
		return toString();
	}

	@Override
	public String toString() {
		return reason;
	}

	@Override
	public Family getFamily() {
		return family;
	}

	public String tojson() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", code);
		map.put("data", reason);
		return Metodos.tojson(map);
	}

}
