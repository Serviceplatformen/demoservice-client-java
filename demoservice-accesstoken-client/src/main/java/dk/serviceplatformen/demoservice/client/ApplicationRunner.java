package dk.serviceplatformen.demoservice.client;

import dk.serviceplatformen.demoservice.client.accesstoken.AccessToken;
import dk.serviceplatformen.demoservice.client.accesstoken.AccessTokenFetcher;
import dk.serviceplatformen.demoservice.client.config.AcccessTokenURL;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class ApplicationRunner {
	private static final Logger logger = Logger.getLogger(ApplicationRunner.class);

	@Autowired
	private AccessTokenFetcher tokenFetcher;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	dk.serviceplatformen.demoservice.client.ErrorHandler.RestTemplateResponseErrorHandler RestTemplateResponseErrorHandler;
	@Autowired
	private AcccessTokenURL acccessTokenURL;

	void callWithToken(String message, String errorMessage) throws Exception {

		String URL = acccessTokenURL.getDemoServiceURL() + message;
		// get the access token
		AccessToken accessToken = tokenFetcher.getAccessToken(acccessTokenURL.getSamlServiceURL());
		// setup request Authorization header
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Holder-of-key " + accessToken.getToken());
		headers.add("x-TransaktionsId", UUID.randomUUID().toString());
		headers.add("x-TransaktionsTid", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity request = new HttpEntity<>("", headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL);
		logger.info("*********Request to Demo Service**************************************");
		if(errorMessage!=null && !errorMessage.equals("")) {
			builder.queryParam("errorMessage", errorMessage);
		}

		logger.info("----------");
		logger.info("Request URL: " + builder.toUriString());
		logger.info("Request Headers: " + request.getHeaders());
		logger.info("----------");

		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters);
		restTemplate.setErrorHandler(RestTemplateResponseErrorHandler);
		ResponseEntity<Object> restServiceResponse = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request,Object.class);

		logger.info("**********Response From Demo Service*********************************");
		logger.info("\n"+restServiceResponse.toString());
	}

}