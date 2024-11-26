/**
 * 
 */
package com.abimulia.secureventure.user.service;

import com.abimulia.secureventure.user.domain.User;
import com.abimulia.secureventure.user.dto.UserDTO;

/**
 * 
 */
public interface UserService {

	UserDTO createUser(User user);

}
