package org.example.todo.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.common.exceptions.ErrorDetails;
import org.example.todo.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
		return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
				|| httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		String objString = new BufferedReader(new InputStreamReader(httpResponse.getBody(), StandardCharsets.UTF_8))
				.lines()
				.collect(Collectors.joining("\n"));
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		ErrorDetails errorDetails = objectMapper.readValue(objString, ErrorDetails.class);

		if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
			log.trace("{}", errorDetails);
			log.error("Received 5XX Error while trying to communicate with service");
		}
		else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			// handle CLIENT_ERROR
			log.trace("{}", errorDetails.getMessage());
			log.debug("Received 4XX Error while trying to communicate with service. {}", errorDetails.getMessage());
			//throw new TestingException(errorDetails.getRequestInformation(), httpResponse.getStatusCode(), "Received 4XX Error while trying to communicate with service.");
//			throw new ResourceNotFoundException()
			if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new ResourceNotFoundException(errorDetails.getMessage());
			}
			if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
//				throw new MyBadRequestException();
			}
			if (httpResponse.getStatusCode() == HttpStatus.CONFLICT) {
//				throw new MyConflictException();
			}
		}
		else {
//			throw new MyApiException(httpResponse.getStatusCode());
			log.error("Received {} Error while trying to communicate with service. {}", errorDetails.getMessage(), httpResponse.getStatusCode());
		}
	}
}
