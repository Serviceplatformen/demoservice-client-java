package dk.serviceplatformen.demoservice.client.sts;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PasswordCallbackHandler implements CallbackHandler {

    private static final String KEYSTORE_PW_PROPERTY = "org.apache.ws.security.crypto.merlin.keystore.password";
    
    private String password;

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callback;
                if (pc.getUsage() == WSPasswordCallback.DECRYPT || pc.getUsage() == WSPasswordCallback.SIGNATURE) {
                    pc.setPassword(getPassword());
                }
            }
        }
    }

    private String getPassword() throws IOException {
        if(password == null) {
            Properties clientProperties = new Properties();
            try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("client.properties")) {
                clientProperties.load(in);
            }
            password = clientProperties.getProperty(KEYSTORE_PW_PROPERTY);
        }
        return password;
    }
}
