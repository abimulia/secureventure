/**
 * 
 */
package com.abimulia.secureventure.user.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.abimulia.secureventure.form.UpdateForm;
import com.abimulia.secureventure.user.domain.User;
import com.abimulia.secureventure.user.dto.UserDTO;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 9:43:20 PM
 * 
 * 
 *            Copyright(c) 2024 Abi Mulia
 */
public interface UserRepository<T extends User> {
	/* Basic CRUD Operations */
	T create(T data);

	Collection<T> list(int page, int pageSize);

	T get(long id);

	T update(T data);

	Boolean delete(Long id);

	/* MAKE FOR LIST */
	List<T> findAll();

	Collection<T> list();

	/* More Complex Operations */
	User getUserByEmail(String email);

	void sendVerificationCode(UserDTO user);

	User verifyCode(String email, String code);

	void resetPassword(String email);

	T verifyPasswordKey(String key);

	void renewPassword(String key, String password, String confirmPassword);

	T verifyAccountKey(String key);

	T updateUserDetails(UpdateForm user);

	void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword);

	void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked);

	User toggleMfa(String email);

	void updateImage(UserDTO user, MultipartFile image);

}
