/**
 * GlobalExceptionHandler.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.exception;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Iterator;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.abimulia.secureventure.domain.HttpResponse;
import com.auth0.jwt.exceptions.JWTDecodeException;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<HttpResponse> handleAllExceptions(Exception ex) {
		log.debug(ex.getMessage());
		HttpResponse errorResponse = HttpResponse.builder().timeStamps(now().toString())
				.message("Oops...I dit it again, let me fix it. Please try again").reason(ex.getMessage())
				.developerMessage(getStackTraceString(ex)).status(INTERNAL_SERVER_ERROR)
				.statusCode(INTERNAL_SERVER_ERROR.value()).build();
		return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
	}

	// Specific handler for UserAlreadyExistsException
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<HttpResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
		log.debug(ex.getMessage());
		HttpResponse errorResponse = HttpResponse.builder().timeStamps(now().toString())
				.message("Duplicate or user already exist").reason(ex.getMessage())
				.developerMessage(getStackTraceString(ex)).status(CONFLICT).statusCode(CONFLICT.value()).build();
		return new ResponseEntity<>(errorResponse, CONFLICT);
	}

	// Specific handler for ApiException
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<HttpResponse> handleApiException(ApiException ex) {
		log.debug(ex.getMessage());
		HttpResponse errorResponse = HttpResponse.builder().timeStamps(now().toString())
				.message("Oops.. something went wrong, let's try again").reason(ex.getMessage())
				.developerMessage(getStackTraceString(ex)).status(CONFLICT).statusCode(CONFLICT.value()).build();
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	// Specific handler for SQLIntegrityConstraintViolationException
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<HttpResponse> sQLIntegrityConstraintViolationException(
			SQLIntegrityConstraintViolationException ex) {
		log.error(ex.getMessage());
		return new ResponseEntity<>(HttpResponse.builder().timeStamps(now().toString())
				.message("Oops... something went wrong, duplicate entry or information already exists")
				.reason(ex.getMessage()).developerMessage(getStackTraceString(ex)).status(BAD_REQUEST)
				.statusCode(BAD_REQUEST.value()).build(), BAD_REQUEST);
	}

	// Specific handler for BadCredentialsException
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<HttpResponse> badCredentialsException(BadCredentialsException ex) {
		log.error(ex.getMessage());
		return new ResponseEntity<>(HttpResponse.builder().timeStamps(now().toString())
				.message("Oops... something went wrong, incorrect email or password").reason(ex.getMessage())
				.developerMessage(getStackTraceString(ex)).status(BAD_REQUEST).statusCode(BAD_REQUEST.value()).build(),
				BAD_REQUEST);
	}

	// Specific handler for AccessDeniedException
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<HttpResponse> accessDeniedException(AccessDeniedException ex) {
		log.error(ex.getMessage());
		return new ResponseEntity<>(HttpResponse.builder().timeStamps(now().toString())
				.message("Oops... Sorry Access denied. You don\'t have access").reason(ex.getMessage())
				.developerMessage(getStackTraceString(ex)).status(FORBIDDEN).statusCode(FORBIDDEN.value()).build(),
				FORBIDDEN);
	}

	// Specific handler for JWTDecodeException
	@ExceptionHandler(JWTDecodeException.class)
	public ResponseEntity<HttpResponse> exception(JWTDecodeException ex) {
		log.error(ex.getMessage());
		return new ResponseEntity<>(HttpResponse.builder().timeStamps(now().toString())
				.message("Could not decode the token").reason(ex.getMessage()).developerMessage(ex.getMessage())
				.status(INTERNAL_SERVER_ERROR).statusCode(INTERNAL_SERVER_ERROR.value()).build(),
				INTERNAL_SERVER_ERROR);
	}

	// Specific handler for EmptyResultDataAccessException
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<HttpResponse> emptyResultDataAccessException(EmptyResultDataAccessException ex) {
		log.error(ex.getMessage());
		return new ResponseEntity<>(HttpResponse.builder().timeStamps(now().toString())
				.message("Oops... Sorry no data found").reason(ex.getMessage())
				.developerMessage(getStackTraceString(ex)).status(BAD_REQUEST).statusCode(BAD_REQUEST.value()).build(),
				BAD_REQUEST);
	}

	// Specific handler for DisabledException
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<HttpResponse> disabledException(DisabledException ex) {
		log.error(ex.getMessage());
		return new ResponseEntity<>(HttpResponse.builder().timeStamps(now().toString())
				.message("Oops... Sorry your account is currently disabled").reason(ex.getMessage())
				.developerMessage(getStackTraceString(ex)).status(BAD_REQUEST).statusCode(BAD_REQUEST.value()).build(),
				BAD_REQUEST);
	}

	// Specific handler for LockedException
	@ExceptionHandler(LockedException.class)
	public ResponseEntity<HttpResponse> lockedException(LockedException ex) {
		log.error(ex.getMessage());
		return new ResponseEntity<>(HttpResponse.builder().timeStamps(now().toString())
				.message("Oops... Sorry you account is currently locked").reason(ex.getMessage())
				.developerMessage(getStackTraceString(ex)).status(BAD_REQUEST).statusCode(BAD_REQUEST.value()).build(),
				BAD_REQUEST);
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<HttpResponse> dataAccessException(DataAccessException ex) {
		log.error(ex.getMessage());
		return new ResponseEntity<>(HttpResponse.builder().timeStamps(now().toString())
				.message("Oops... Sorry error an accesing data").reason(ex.getMessage())
				.developerMessage(getStackTraceString(ex)).status(BAD_REQUEST).statusCode(BAD_REQUEST.value()).build(),
				BAD_REQUEST);
	}

	private String getStackTraceString(Exception ex) {
		StackTraceElement[] stackTraceElements = ex.getStackTrace();
		int maxIndex = stackTraceElements.length;
		if (maxIndex > 5) {
			maxIndex = 5;
		}
		StringBuilder stackTraceString = new StringBuilder();
		for (int i = 0; i < stackTraceElements.length; i++) {
			stackTraceString.append(stackTraceElements[i].toString()).append("\n");
		}
		return stackTraceString.toString();
	}
}
