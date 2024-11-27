/**
 * 
 */
package com.abimulia.secureventure.user.service;

import static com.abimulia.secureventure.user.mapper.UserDTOMapper.fromUser;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abimulia.secureventure.form.UpdateForm;
import com.abimulia.secureventure.user.domain.Role;
import com.abimulia.secureventure.user.domain.User;
import com.abimulia.secureventure.user.dto.UserDTO;
import com.abimulia.secureventure.user.mapper.UserDTOMapper;
import com.abimulia.secureventure.user.repository.RoleRepository;
import com.abimulia.secureventure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	

	private final UserRepository<User> userRepository;
	private final RoleRepository<Role> roleRoleRepository;

	@Override
	public UserDTO createUser(User user) {
		return UserDTOMapper.fromUser(userRepository.create(user));
	}

	@Override
	public boolean deleteUser(Long id) {
        return userRepository.delete(id);
	}

	@Override
	public UserDTO listUsers() {
		return mapToUserDTO((User) userRepository.findAll());
	}

	@Override
	public Collection<UserDTO> list() {
		return maptoUserDTOList(userRepository.list());
	}

	@Override
	public UserDTO getUserByEmail(String email) {
		return mapToUserDTO(userRepository.getUserByEmail(email));
	}

	@Override
	public void sendVerificationCode(UserDTO user) {
		userRepository.sendVerificationCode(user);
		
	}

	@Override
	public UserDTO verifyCode(String email, String code) {
		return mapToUserDTO(userRepository.verifyCode(email, code));
	}

	@Override
	public void resetPassword(String email) {
		userRepository.resetPassword(email);
		
	}

	@Override
	public UserDTO verifyPasswordKey(String key) {
		return mapToUserDTO(userRepository.verifyPasswordKey(key));
	}

	@Override
	public void renewPassword(String key, String password, String confirmPassword) {
		userRepository.renewPassword(key, password, confirmPassword);
		
	}

	@Override
	public UserDTO verifyAccountKey(String key) {
		return mapToUserDTO(userRepository.verifyAccountKey(key));
	}

	@Override
	public UserDTO updateUserDetails(UpdateForm user) {
		return mapToUserDTO(userRepository.updateUserDetails(user));
	}

	@Override
	public UserDTO getUserById(Long userId) {
		return mapToUserDTO(userRepository.get(userId));
	}

	@Override
	public void updatePassword(Long userId, String currentPassword, String newPassword, String confirmNewPassword) {
		userRepository.updatePassword(userId, currentPassword, newPassword, confirmNewPassword);
		
	}

	@Override
	public void updateUserRole(Long userId, String roleName) {
		roleRoleRepository.updateUserRole(userId, roleName);
		
	}

	@Override
	public void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked) {
		userRepository.updateAccountSettings(userId, enabled, notLocked);
		
	}

	@Override
	public UserDTO toggleMfa(String email) {
		return mapToUserDTO(userRepository.toggleMfa(email));
	}

	@Override
	public void updateImage(UserDTO user, MultipartFile image) {
		userRepository.updateImage(user, image);
	}
	
	private UserDTO mapToUserDTO(User user) {
        return fromUser(user, roleRoleRepository.getRoleByUserId(user.getId()));
    }
	
	private Collection<UserDTO> maptoUserDTOList(Collection<User> users) {
        Collection<UserDTO> userDTOS = new ArrayList<>();
        users.forEach(user -> {
            userDTOS.add(fromUser(user, roleRoleRepository.getRoleByUserId(user.getId())));
        });
        return userDTOS;
    }

}
