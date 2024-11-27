/**
 * UpdatePasswordForm.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 4:05:10 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@Getter
@Setter
public class UpdatePasswordForm {
	@NotEmpty(message = "Current Password cannot be empty")
	private String currentPassword;
	@NotEmpty(message = "New password cannot be empty")
	private String newPassword;
	@NotEmpty(message = "Confirm password cannot be empty")
	private String confirmNewPassword;
}
