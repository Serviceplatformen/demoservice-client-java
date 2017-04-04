package dk.serviceplatformen.demoservice.client.sts;

import org.apache.cxf.Bus;
import org.apache.cxf.staxutils.W3CDOMStreamWriter;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.xml.security.utils.Base64;

import java.security.cert.X509Certificate;

public class STSClient extends org.apache.cxf.ws.security.trust.STSClient {

    public STSClient(Bus b) {
        super(b);
    }

    @Override
    protected STSResponse issue(String appliesTo, String action, String requestType, String binaryExchange) throws Exception {
        // the STS does not like the CXF generated Content-Type header, so we overwrite with a custom one that the STS likes
        ((HTTPConduit) getClient().getConduit()).getClient().setContentType("application/soap+xml; charset=utf-8");

        return super.issue(appliesTo, action, requestType, binaryExchange);
    }

    @Override
    protected void writeElementsForRSTPublicKey(W3CDOMStreamWriter writer, X509Certificate cert) throws Exception {
        writer.writeStartElement("wst", "UseKey", namespace);
        writer.writeStartElement("wsse", "BinarySecurityToken", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        writer.writeAttribute("EncodingType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
        writer.writeAttribute("ValueType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");

        // the KOMBIT STS requires that the UseKey contains a BinarySecurityToken, so the usual X509Data that the CXF can supply will not work
        String encodedCert = Base64.encode(cert.getEncoded());
        writer.writeCharacters(encodedCert);

        writer.writeEndElement();
        writer.writeEndElement();
    }
}