package org.example.todo.common.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
@ToString
@Slf4j
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDetails {

	@EqualsAndHashCode.Exclude
	private final OffsetDateTime timestamp;
	private final String message;
	private final List<String> details;
	//Error Code
	private final String requestInformation;
	private final String errorCode;
	//Link to more info
	private final String moreInfo;

	public ErrorDetails(OffsetDateTime timestamp, String message, List<String> details, String requestInformation) {
		this.timestamp = timestamp;
		this.message = message;
		//Clone list to prevent mutable object edits
		this.details = Objects.nonNull(details) ? new ArrayList<>(details) : null;
		this.errorCode = "12345";
		this.moreInfo = "http://ourDocumentationWebsite.org/docs/errors/12345";
		this.requestInformation = requestInformation;

	}
}
