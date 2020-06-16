package org.example.todo.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		// This allows us to read the response more than once - Necessary for debugging.
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(restTemplate.getRequestFactory()));
		restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
//		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(username, password));
		return restTemplate;
	}
}
