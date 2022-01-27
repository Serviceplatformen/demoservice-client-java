package dk.serviceplatformen.demoservice.client.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import dk.serviceplatformen.demoservice.client.config.AcccessTokenURL;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ImportResource({ "classpath:cxf.xml" })
@PropertySource("classpath:client.properties")
public class ClientConfig {

	@Value("${org.apache.ws.security.crypto.merlin.file}")
	private Resource keystore;

	@Value("${org.apache.ws.security.crypto.merlin.keystore.password}")
	private String keystorePasswd;

	@Value("${org.apache.ws.security.crypto.merlin.truststore.type}")
	private String keystoreType;

	@Value("${accesstokenservice.url}")
	private String authServiceURL;

	@Value("${demoservice.url}")
	private String demoServiceURL;
	
	@Value("${service.entityid}")
	private String samlServiceURL;

	@Bean
	public ClientHttpRequestFactory httpComponentsClientHttpRequestFactory() throws NoSuchAlgorithmException,
			KeyStoreException, KeyManagementException, UnrecoverableKeyException, CertificateException, IOException {

		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		// SSLContextBuilder builder = new SSLContextBuilder();
		SSLContextBuilder builder =  SSLContextBuilder.create();
		
		builder.useProtocol("TLSv1.2");
		
		
		builder.loadTrustMaterial(null, acceptingTrustStrategy);
		

		InputStream inputStream = keystore.getInputStream();
		KeyStore ks = KeyStore.getInstance(keystoreType);
		ks.load(inputStream, keystorePasswd.toCharArray());
		builder.loadKeyMaterial(ks, keystorePasswd.toCharArray());

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

		
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.setSSLSocketFactory(sslsf).build();

		
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		return new BufferingClientHttpRequestFactory(requestFactory);

	}

	@Bean
	public AcccessTokenURL getAccessTokenURL() {
		AcccessTokenURL url = new AcccessTokenURL();

		url.setAuthServiceURL(authServiceURL);
		url.setDemoServiceURL(demoServiceURL);
		url.setSamlServiceURL(samlServiceURL);

		return url;
	}

	@Bean
	public RestTemplate restTemplate() throws Exception {

		ClientHttpRequestFactory requestFactory = httpComponentsClientHttpRequestFactory();

		RestTemplate template = new RestTemplate(requestFactory);

		return template;
	}

}
