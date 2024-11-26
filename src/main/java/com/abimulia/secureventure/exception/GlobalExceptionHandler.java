/**
 * GlobalExceptionHandler.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.abimulia.secureventure.domain.HttpResponse;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 2:34:56 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<HttpResponse> handleAllExceptions(Exception ex) {
		HttpResponse errorResponse = HttpResponse.builder().timeStamps(LocalDateTime.now().toString())
				.status(HttpStatus.INTERNAL_SERVER_ERROR).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message("An error occurred").developerMessage(ex.getMessage()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Specific handler for UserAlreadyExistsException:
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<HttpResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
		HttpResponse errorResponse = HttpResponse.builder().timeStamps(LocalDateTime.now().toString())
				.status(HttpStatus.CONFLICT).statusCode(HttpStatus.CONFLICT.value()).message("User already exist")
				.developerMessage(ex.getMessage()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	// Specific handler for ApiException:s
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<HttpResponse> handleApiException(ApiException ex) {
		HttpResponse errorResponse = HttpResponse.builder().timeStamps(LocalDateTime.now().toString())
				.status(HttpStatus.CONFLICT).statusCode(HttpStatus.CONFLICT.value()).message("Failed fetching data")
				.developerMessage(ex.getMessage()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

}
