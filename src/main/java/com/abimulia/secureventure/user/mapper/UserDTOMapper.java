/**
 * 
 */
package com.abimulia.secureventure.user.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.abimulia.secureventure.user.domain.User;
import com.abimulia.secureventure.user.dto.UserDTO;

/**
 * 
 */
@Component
public class UserDTOMapper {
	public static UserDTO fromUser(User user) {
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(user, userDTO);
		return userDTO;
	}
	
	public static User toUser(UserDTO userDTO) {
		User user = new User();
		BeanUtils.copyProperties(userDTO, user);
		return user;
	}

}
