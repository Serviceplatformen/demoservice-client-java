package dk.serviceplatformen.demoservice.client.sts;

import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.ws.security.trust.claims.ClaimsCallback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

public class ClaimsCallbackHandler implements CallbackHandler {

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof ClaimsCallback) {
				ClaimsCallback callback = (ClaimsCallback) callbacks[i];
				callback.setClaims(createClaims());
			}
			else {
				throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
			}
		}
	}

	private static Element createClaims() {
		Document doc = DOMUtils.createDocument();
		Element claimsElement = doc.createElementNS("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "Claims");
		claimsElement.setAttributeNS(null, "Dialect", "http://docs.oasis-open.org/wsfed/authorization/200706/authclaims");

		Element claimType = doc.createElementNS("http://docs.oasis-open.org/wsfed/authorization/200706", "ClaimType");
		claimType.setAttributeNS(null, "Uri", "dk:gov:saml:attribute:CvrNumberIdentifier");
		claimsElement.appendChild(claimType);

		Element claimValue = doc.createElementNS("http://docs.oasis-open.org/wsfed/authorization/200706", "Value");
		claimValue.setTextContent("29189846");
		claimType.appendChild(claimValue);

		return claimsElement;
	}
}
