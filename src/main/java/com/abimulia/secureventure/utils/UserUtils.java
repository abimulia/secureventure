/**
 * UserUtils.java
 * 27-Nov-2024
 */
package com.abimulia.secureventure.utils;

import org.springframework.security.core.Authentication;

import com.abimulia.secureventure.domain.UserPrincipal;
import com.abimulia.secureventure.user.dto.UserDTO;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (27-Nov-2024)
 * @since 27-Nov-2024 12:12:27 AM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
public class UserUtils {
	public static UserDTO getAuthenticatedUser(Authentication authentication) {
		return ((UserDTO) authentication.getPrincipal());
	}

	public static UserDTO getLoggedInUser(Authentication authentication) {
		return ((UserPrincipal) authentication.getPrincipal()).getUser();
	}

}
