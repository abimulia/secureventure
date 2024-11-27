/**
 * LoginForm.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 4:03:18 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@Getter
@Setter
public class LoginForm {
	@NotEmpty(message = "Email cannot be empty")
	@Email(message = "Invalid email. Please enter a valid email address")
	private String email;
	@NotEmpty(message = "Password cannot be empty")
	private String password;
}