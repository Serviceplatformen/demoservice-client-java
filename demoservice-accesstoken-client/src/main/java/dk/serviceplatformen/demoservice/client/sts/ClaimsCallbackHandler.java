package dk.serviceplatformen.demoservice.client.sts;


import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.ws.security.trust.claims.ClaimsCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

@Component
@PropertySource("classpath:client.properties")
public class ClaimsCallbackHandler implements CallbackHandler {
	
	@Value("${municipality.cvr}")
    private String municipalityCvr;
	
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof ClaimsCallback) {
				final ClaimsCallback callback = (ClaimsCallback) callbacks[i];
				callback.setClaims(createClaims());
			}
			else {
				throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
			}
		}
	}

	private Element createClaims() {
		final Document doc = DOMUtils.createDocument();
		final Element claimsElement = doc.createElementNS("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "Claims");
		claimsElement.setAttributeNS(null, "Dialect", "http://docs.oasis-open.org/wsfed/authorization/200706/authclaims");

		final Element claimType = doc.createElementNS("http://docs.oasis-open.org/wsfed/authorization/200706", "ClaimType");
		claimType.setAttributeNS(null, "Uri", "dk:gov:saml:attribute:CvrNumberIdentifier");
		claimsElement.appendChild(claimType);

		final Element claimValue = doc.createElementNS("http://docs.oasis-open.org/wsfed/authorization/200706", "Value");
		claimValue.setTextContent(municipalityCvr);
		claimType.appendChild(claimValue);

		return claimsElement;
	}
}
