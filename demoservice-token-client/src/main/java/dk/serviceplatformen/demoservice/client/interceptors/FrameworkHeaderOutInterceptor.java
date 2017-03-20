package dk.serviceplatformen.demoservice.client.interceptors;

import dk.serviceplatformen.demoservice.client.interceptors.model.SbfFrameworkHeader;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.xml.XMLFault;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.phase.Phase;

import javax.xml.namespace.QName;
import java.util.List;

public class FrameworkHeaderOutInterceptor extends AbstractSoapInterceptor {

    public FrameworkHeaderOutInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }

    @Override
    public void handleMessage(SoapMessage soapMessage) throws Fault {
        List<Header> headers = soapMessage.getHeaders();

        try {
            Header framework = new SoapHeader(new QName("urn:liberty:sb:2006-08", "Framework", "sbf"), new SbfFrameworkHeader(), new JAXBDataBinding(SbfFrameworkHeader.class));
            headers.add(framework);
        } catch (Exception ex) {
            throw XMLFault.createFault(new Fault(ex));
        }
        soapMessage.put(Header.HEADER_LIST, headers);
    }
}
