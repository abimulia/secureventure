/**
 * UpdateForm.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
* 
* @author abimu
*
* @version 1.0 (26-Nov-2024)
* @since 26-Nov-2024 4:01:54 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
@Getter
@Setter
public class UpdateForm {
	@NotNull(message = "ID cannot be null or empty")
    private Long id;
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
    private String email;
    @Pattern(regexp = "^\\d{11}$", message = "Invalid phone number")
    private String phone;
    private String address;
    private String title;
    private String bio;
}
