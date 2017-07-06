package dk.serviceplatformen.demoservice.client;

import dk.serviceplatformen.demoservice.client.factories.AuthorityContextRequestFactory;
import dk.serviceplatformen.demoservice.client.factories.InvocationContextRequestFactory;
import dk.serviceplatformen.xml.wsdl.soap11.sp.demo._1.CallDemoServiceRequestType;
import dk.serviceplatformen.xml.wsdl.soap11.sp.demo._1.CallDemoServiceResponseType;
import dk.serviceplatformen.xml.wsdl.soap11.sp.demo._1.DemoPortType;
import dk.serviceplatformen.xml.wsdl.soap11.sp.demo._1.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.BindingProvider;

@Component
public class ApplicationRunner {

    private static final String ENDPOINT_URL = "https://exttest.serviceplatformen.dk/service/SP/Demo/1";

    @Autowired
    private InvocationContextRequestFactory invocationContextRequestFactory;

    @Autowired
    private AuthorityContextRequestFactory authorityContextRequestFactory;

    private DemoPortType demoPort;

    public ApplicationRunner() {
        init();
    }

    private void init() {
        final DemoPortType port = new DemoService().getDemoPort();
        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ENDPOINT_URL);
        demoPort = port;
    }

    String callWithAuthorityContext(String message) {
        final CallDemoServiceRequestType request = authorityContextRequestFactory.getDemoServiceRequestType(message);
        return callDemoService(request);
    }

    String callWithInvocationContext(String message) {
        final CallDemoServiceRequestType request = invocationContextRequestFactory.getDemoServiceRequestType(message);
        return callDemoService(request);
    }

    private String callDemoService(CallDemoServiceRequestType request) {
        try {
            final CallDemoServiceResponseType response = demoPort.callDemoService(request);
            return response.getResponseString();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}