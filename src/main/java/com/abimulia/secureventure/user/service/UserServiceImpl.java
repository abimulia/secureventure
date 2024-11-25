/**
 * 
 */
package com.abimulia.secureventure.user.service;

import org.springframework.stereotype.Service;

import com.abimulia.secureventure.user.domain.User;
import com.abimulia.secureventure.user.dto.UserDTO;
import com.abimulia.secureventure.user.mapper.UserDTOMapper;
import com.abimulia.secureventure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository<User> userRepository;

	@Override
	public UserDTO createUser(User user) {
		return UserDTOMapper.fromUser(userRepository.create(user));
	}

}
