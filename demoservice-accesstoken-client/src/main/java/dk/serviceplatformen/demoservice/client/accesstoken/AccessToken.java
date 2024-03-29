package dk.serviceplatformen.demoservice.client.accesstoken;

import com.fasterxml.jackson.annotation.JsonSetter;

public class AccessToken {
	private String token;
	private String tokenType;
	private int expiresIn;

	public String getToken() {
		return token;
	}

	@JsonSetter("access_token")
	public void setToken(String accessToken) {
		this.token = accessToken;
	}

	@Override
	public String toString() {
		return "AccessToken [token=" + token + ", tokenType=" + tokenType + ", expiresIn=" + expiresIn + "]";
	}

	public String getTokenType() {
		return tokenType;
	}

	@JsonSetter("token_type")
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	@JsonSetter("expires_in")
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
}
