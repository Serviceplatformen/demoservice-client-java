package dk.serviceplatformen.demoservice.client.factories;

import dk.serviceplatformen.xml.schemas.callcontext._1.CallContextType;
import dk.serviceplatformen.xml.wsdl.soap11.sp.demo._1.CallDemoServiceRequestType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@PropertySource(value = "classpath:token.properties")
public class TokenRequestFactory {

    @Value("${municipality.cvr}")
    private String municipalityCvr;

    @Value("${accounting.info}")
    private String accountingInfo;

    @Value("${callers.service.call.identifier}")
    private String callersServiceCallIdentifier;

    @Value("${on.behalf.of.user}")
    private String onBehalfOfUser;

    public static TokenRequestFactory getInstance() {
        return SpringAccessor.getApplicationContext().getBean(TokenRequestFactory.class);
    }

    public CallDemoServiceRequestType getDemoServiceRequestType(String requestMessage) {
        final CallDemoServiceRequestType demoServiceRequestType = new CallDemoServiceRequestType();
        final CallContextType callContextType = getCallContext();
        demoServiceRequestType.setCallContext(callContextType);
        demoServiceRequestType.setMessageString(requestMessage);
        return demoServiceRequestType;
    }

    public String getMunicipalityCvr() {
        return municipalityCvr;
    }

    private CallContextType getCallContext() {
        final CallContextType callContextType = new CallContextType();
        callContextType.setAccountingInfo(accountingInfo);
        callContextType.setCallersServiceCallIdentifier(callersServiceCallIdentifier);
        callContextType.setOnBehalfOfUser(onBehalfOfUser);
        return callContextType;
    }

    @Component
    private static class SpringAccessor implements ApplicationContextAware {

        private static ApplicationContext applicationContext;

        private static ApplicationContext getApplicationContext() {
            return applicationContext;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }
}
