package dk.serviceplatformen.demoservice.client.interceptors;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.CheckFaultInterceptor;
import org.apache.cxf.binding.xml.XMLFault;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;

import javax.xml.namespace.QName;
import java.util.HashSet;
import java.util.Set;

public class UnderstandFrameworkHeaderInInterceptor extends AbstractSoapInterceptor {

    private static final QName frameworkQname = new QName("urn:liberty:sb:2006-08", "Framework", "sbf");

    public UnderstandFrameworkHeaderInInterceptor() {
        super(Phase.POST_PROTOCOL); // Check for faults first, in order not to override those.
        addAfter(CheckFaultInterceptor.class.getName());
    }

    @Override
    public void handleMessage(SoapMessage message) {
        Header framework = message.getHeader(frameworkQname);
        if(framework == null && !MessageUtils.isFault(message)) {
            throw new XMLFault("Missing Framework header");
        }
    }

    @Override
    public Set<QName> getUnderstoodHeaders() {
        Set<QName> set = new HashSet<>();
        set.add(frameworkQname);
        return set;
    }
}