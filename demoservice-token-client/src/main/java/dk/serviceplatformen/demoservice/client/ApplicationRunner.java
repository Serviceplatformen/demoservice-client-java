package dk.serviceplatformen.demoservice.client;

import dk.serviceplatformen.demoservice.client.factories.TokenRequestFactory;
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
    private TokenRequestFactory tokenContextRequestFactory;
    private DemoPortType demoPortType;

    public ApplicationRunner() {
        init();
    }

    private void init() {
        DemoPortType demoPort = new DemoService().getDemoPort();
        BindingProvider bp = (BindingProvider) demoPort;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ENDPOINT_URL);
        demoPortType = (DemoPortType) bp;
    }

    void callWithToken(String message) {
        try {
            CallDemoServiceRequestType request = tokenContextRequestFactory.getDemoServiceRequestType(message);
            CallDemoServiceResponseType response = demoPortType.callDemoService(request);
            System.out.println("Service reply from demoService call with token security: " + response.getResponseString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}