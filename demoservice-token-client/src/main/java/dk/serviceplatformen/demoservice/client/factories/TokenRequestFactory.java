package dk.serviceplatformen.demoservice.client.factories;

import dk.serviceplatformen.xml.schemas.callcontext._1.CallContextType;
import dk.serviceplatformen.xml.wsdl.soap11.sp.demo._1.CallDemoServiceRequestType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:token.properties")
public class TokenRequestFactory {

    @Value("${accounting.info}")
    private String accountingInfo;

    @Value("${callers.service.call.identifier}")
    private String callersServiceCallIdentifier;

    @Value("${on.behalf.of.user}")
    private String onBehalfOfUser;

    public CallDemoServiceRequestType getDemoServiceRequestType(String requestMessage) {
        CallDemoServiceRequestType demoServiceRequestType = new CallDemoServiceRequestType();
        CallContextType callContextType = getCallContext();
        demoServiceRequestType.setCallContext(callContextType);
        demoServiceRequestType.setMessageString(requestMessage);
        return demoServiceRequestType;
    }

    private CallContextType getCallContext() {
        CallContextType callContextType = new CallContextType();
        callContextType.setAccountingInfo(accountingInfo);
        callContextType.setCallersServiceCallIdentifier(callersServiceCallIdentifier);
        callContextType.setOnBehalfOfUser(onBehalfOfUser);
        return callContextType;
    }
}
