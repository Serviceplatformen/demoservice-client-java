package dk.serviceplatformen.demoservice.client.config;

public class AcccessTokenURL {

	private String authServiceURL;
	public String getAuthServiceURL() {
		return authServiceURL;
	}
	public void setAuthServiceURL(String authServiceURL) {
		this.authServiceURL = authServiceURL;
	}
	public String getDemoServiceURL() {
		return demoServiceURL;
	}
	public void setDemoServiceURL(String demoServiceURL) {
		this.demoServiceURL = demoServiceURL;
	}
	private String demoServiceURL;
	
	private String samlServiceURL;
	public String getSamlServiceURL() {
		return samlServiceURL;
	}
	public void setSamlServiceURL(String samlServiceURL) {
		this.samlServiceURL = samlServiceURL;
	}
	
	
	
	
}
