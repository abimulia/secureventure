/**
 * UserAlreadyExistsException.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.exception;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 2:36:53 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
public class UserAlreadyExistsException extends RuntimeException {

	public UserAlreadyExistsException(String message) {
		super(message);
	}

}
