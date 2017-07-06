package dk.serviceplatformen.demoservice.client.factories;

import dk.serviceplatformen.xml.schemas.invocationcontext._1.InvocationContextType;
import dk.serviceplatformen.xml.wsdl.soap11.sp.demo._1.CallDemoServiceRequestType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value="classpath:invocationcontext.properties")
public class InvocationContextRequestFactory {

    @Value("${service.agreement.uuid}")
    private String serviceAgreementUUID;

    @Value("${user.system.uuid}")
    private String userSystemUUID;

    @Value("${service.uuid}")
    private String serviceUUID;

    @Value("${user.uuid}")
    private String userUUID;

    @Value("${accounting.info}")
    private String accountingInfo;

    @Value("${callers.service.call.identifier}")
    private String callersServiceCallIdentifier;

    @Value("${on.behalf.of.user}")
    private String onBehalfOfUser;

    public CallDemoServiceRequestType getDemoServiceRequestType(String requestMessage) {
        CallDemoServiceRequestType demoServiceRequestType = new CallDemoServiceRequestType();
        demoServiceRequestType.setMessageString(requestMessage);
        InvocationContextType invocationContextType = getInvocationContext();
        demoServiceRequestType.setInvocationContext(invocationContextType);
        return demoServiceRequestType;
    }

    private InvocationContextType getInvocationContext() {
        InvocationContextType invocationContextType = new InvocationContextType();
        invocationContextType.setServiceAgreementUUID(serviceAgreementUUID);
        invocationContextType.setUserSystemUUID(userSystemUUID);
        invocationContextType.setServiceUUID(serviceUUID);
        invocationContextType.setUserUUID(userUUID);
        invocationContextType.setAccountingInfo(accountingInfo);
        invocationContextType.setCallersServiceCallIdentifier(callersServiceCallIdentifier);
        invocationContextType.setOnBehalfOfUser(onBehalfOfUser);
        return invocationContextType;
    }
}
