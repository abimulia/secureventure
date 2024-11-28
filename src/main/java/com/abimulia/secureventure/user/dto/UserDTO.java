/**
 * 
 */
package com.abimulia.secureventure.user.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (28-Nov-2024)
 * @since 28-Nov-2024 9:35:24 AM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
@Data
public class UserDTO {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String phone;
	private String title;
	private String bio;
	private String imageUrl;
	private boolean enabled;
	private boolean isNotLocked;
	private boolean isUsingMfa;
	private LocalDateTime createdDate;
	private LocalDateTime updateDate;
	private String roleName;
	private String permissions;

}
