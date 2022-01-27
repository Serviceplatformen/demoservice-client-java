package dk.serviceplatformen.demoservice.client.accesstoken;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.jodah.expiringmap.ExpiringMap;

import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;

import dk.serviceplatformen.demoservice.client.ApplicationRunner;
import dk.serviceplatformen.demoservice.client.config.AcccessTokenURL;
import dk.serviceplatformen.demoservice.client.sts.STSClient;

@Component
@ImportResource({ "classpath:cxf.xml" })
public class AccessTokenFetcher {
	
	private static final Logger logger = Logger.getLogger(AccessTokenFetcher.class);
	private ExpiringMap<String, AccessToken> accessTokenCache = ExpiringMap.builder().expiration(55, TimeUnit.MINUTES).build();
	private ExpiringMap<String, SecurityToken> samlTokenCache = ExpiringMap.builder().expiration(7, TimeUnit.HOURS).build();

	@Autowired
	public dk.serviceplatformen.demoservice.client.sts.STSClient stsClient;
	
	@Autowired
	private AcccessTokenURL acccessTokenURL;

	@Autowired
	public RestTemplate restTemplate;
	
	public AccessToken getAccessToken(String audience) throws Exception {
		AccessToken accessToken = accessTokenCache.get(audience);

		if (accessToken == null) {
			SecurityToken samlToken = samlTokenCache.get(audience);

			if (samlToken == null) {
				samlToken = stsClient.requestSecurityToken(audience);
				
				samlTokenCache.put(audience, samlToken);
			}

			String base64EncodedToken = getEncodedSamlToken(samlToken);

			String simpleURL = acccessTokenURL.getAuthServiceURL();

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

			MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();

			body.add("saml-token", base64EncodedToken);

			HttpEntity request = new HttpEntity<>(body, headers);

			logger.info("*********Request to Authorization Service**************************************");

			logger.info("Request Details: " + request);

			ResponseEntity<AccessToken> authorizationServiceResponse = restTemplate
					.exchange(simpleURL, HttpMethod.POST, request, AccessToken.class);

			logger.info("**********Response From Authorization Service*********************************");
			logger.info("");
			logger.info("Response : " + authorizationServiceResponse);

			accessToken = authorizationServiceResponse.getBody();

			accessTokenCache.put(audience, accessToken);
		}

		return accessToken;
	}

	static String getEncodedSamlToken(SecurityToken token) throws Exception {
		Document doc = token.getToken().getOwnerDocument();

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String samlTokenAsString = writer.getBuffer().toString();
		
		System.out.println("samlTokenAsString = " + samlTokenAsString );

		return Base64.getEncoder().encodeToString(samlTokenAsString.getBytes(StandardCharsets.UTF_8));
	}
	
/*	private static String encodeValue(String value) throws UnsupportedEncodingException {
	    return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
	} */
}
