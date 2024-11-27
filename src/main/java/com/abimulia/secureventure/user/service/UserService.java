/**
 * 
 */
package com.abimulia.secureventure.user.service;

import java.util.Collection;

import org.springframework.web.multipart.MultipartFile;

import com.abimulia.secureventure.form.UpdateForm;
import com.abimulia.secureventure.user.domain.User;
import com.abimulia.secureventure.user.dto.UserDTO;

/**
 * 
 */
public interface UserService {

	UserDTO createUser(User user);

	boolean deleteUser(Long id);

	UserDTO listUsers();

	Collection<UserDTO> list();

	UserDTO getUserByEmail(String email);

	void sendVerificationCode(UserDTO user);

	UserDTO verifyCode(String email, String code);

	void resetPassword(String email);

	UserDTO verifyPasswordKey(String key);

	void renewPassword(String key, String password, String confirmPassword);

	UserDTO verifyAccountKey(String key);

	UserDTO updateUserDetails(UpdateForm user);

	UserDTO getUserById(Long userId);

	void updatePassword(Long userId, String currentPassword, String newPassword, String confirmNewPassword);

	void updateUserRole(Long userId, String roleName);

	void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked);

	UserDTO toggleMfa(String email);

	void updateImage(UserDTO user, MultipartFile image);

}
