package com.abimulia.secureventure.user.repository;

import static com.abimulia.secureventure.enums.VerificationType.ACCOUNT;
import static com.abimulia.secureventure.enums.VerificationType.PASSWORD;
import static com.abimulia.secureventure.user.enums.RoleType.ROLE_USER;
import static com.abimulia.secureventure.user.query.SqlQuery.COUNT_USER_EMAIL_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.DELETE_CODE;
import static com.abimulia.secureventure.user.query.SqlQuery.DELETE_FROM_USERS_BY_USER_ID;
import static com.abimulia.secureventure.user.query.SqlQuery.DELETE_PASSWORD_VERIFICATION_BY_USER_ID_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.DELETE_VERIFICATION_BY_URL_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.DELETE_VERIFICATION_CODE_BY_USER_ID;
import static com.abimulia.secureventure.user.query.SqlQuery.INSERT_ACCOUNT_VERIFICATION_URL_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.INSERT_PASSWORD_VERIFICATION_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.INSERT_USER_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.INSERT_VERIFICATION_CODE_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.SELECT_ALL_USER;
import static com.abimulia.secureventure.user.query.SqlQuery.SELECT_CODE_EXPIRATION_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.SELECT_EXPIRATION_BY_URL;
import static com.abimulia.secureventure.user.query.SqlQuery.SELECT_USER_BY_ACCOUNT_URL_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.SELECT_USER_BY_EMAIL_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.SELECT_USER_BY_ID_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.SELECT_USER_BY_PASSWORD_URL_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.SELECT_USER_BY_USER_CODE_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.TOGGLE_USER_MFA_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.UPDATE_USER_DETAILS_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.UPDATE_USER_ENABLED_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.UPDATE_USER_IMAGE_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.UPDATE_USER_PASSWORD_BY_ID_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.UPDATE_USER_PASSWORD_BY_URL_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.UPDATE_USER_SETTINGS_QUERY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.time.DateFormatUtils.format;
import static org.apache.commons.lang3.time.DateUtils.addDays;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abimulia.secureventure.domain.UserPrincipal;
import com.abimulia.secureventure.enums.VerificationType;
import com.abimulia.secureventure.exception.ApiException;
import com.abimulia.secureventure.exception.UserAlreadyExistsException;
import com.abimulia.secureventure.form.UpdateForm;
import com.abimulia.secureventure.user.domain.Role;
import com.abimulia.secureventure.user.domain.User;
import com.abimulia.secureventure.user.dto.UserDTO;
import com.abimulia.secureventure.user.mapper.UserRowMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* 
* @author abimu
*
* @version 1.0 (26-Nov-2024)
* @since 26-Nov-2024 2:07:53 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User>,UserDetailsService {
	private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
	private final NamedParameterJdbcTemplate jdbc;
	private final RoleRepository<Role> roleRepository;
	private final BCryptPasswordEncoder encoder;

	@Override
	public User create(User user) {
		log.debug("create() " + user);
		if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0)
			throw new UserAlreadyExistsException("Email already in used. Please use a different email and try again");
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource params = getSqlParameterSource(user);
			jdbc.update(INSERT_USER_QUERY, params, keyHolder);
			user.setId(requireNonNull(keyHolder.getKey().longValue()));
			roleRepository.addRoleToUser(user.getId(),ROLE_USER.name());
			String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(),ACCOUNT.getType());
			jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userId", user.getId(), "url", verificationUrl));
//			emailService.sendVerificationUrl(user.getFirstName(),user.getEmail(),verificationUrl, ACCOUNT);
			user.setEnabled(false);
			user.setNotLocked(true);
			log.info( "User {} created with role {} ",user.getFirstName(),ROLE_USER.name());
			return user;
		} catch (EmptyResultDataAccessException emptyException) {
			log.error("Error creating user, " + emptyException.getMessage());
			throw new ApiException(ROLE_USER.name()+ " Role not exists.",emptyException);
		} 
		catch (Exception e) {
			log.error("Error creating user, " + e.getMessage());
			throw new ApiException("Oops ... ",e);
		}
	}

	@Override
	public Collection<User> list(int page, int pageSize) {
		log.debug("list() " + page + " pageSize: " + pageSize);
		// TODO Auto-generated method stub, not yet #1
		return null;
	}

	@Override
	public User get(long id) {
		log.debug("get() user_id" + id);
		try {
            return jdbc.queryForObject(SELECT_USER_BY_ID_QUERY, of("id", id), new UserRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No User found by id: " + id);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again. ",exception);
        }
	}

	@Override
	public User update(User data) {
		log.debug("Update() userData" + data);
		// TODO Auto-generated method stub, not yet#2
		return null;
	}

	@Override
	public Boolean delete(Long id) {
		log.debug("delete() user_id" + id);
		try {
            jdbc.update(DELETE_FROM_USERS_BY_USER_ID, Collections.singletonMap("userId", id));
            return true;
        }
        catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again. ",exception);
        }
	}

	

	@Override
	public List<User> findAll() {
		log.debug("findAll() ");
		 try {
	            List<User> users = jdbc.query(SELECT_ALL_USER, new UserRowMapper());
	            return users;
	        } catch (Exception exception) {
	            log.error(exception.getMessage());
	            throw new ApiException("An error occurred while retrieving the list of users. Please try again. ",exception);
	        }
	}

	@Override
	public Collection<User> list() {
		log.debug("list() ");
		try {
            return jdbc.query(SELECT_ALL_USER, new UserRowMapper());
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while retrieving the list of users. Please try again. ",exception);
        }
	}

	@Override
	public User getUserByEmail(String email) {
		log.debug("getUserByEmail() email:" + email);
		try {
            User user = jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, of("email", email), new UserRowMapper());
            return user;
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No User found by email: " + email,exception);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.",exception);
        }
	}

	@Override
	public void sendVerificationCode(UserDTO user) {
		log.debug("sendVerificationCode() user: " + user);
		String expirationDate = format(addDays(new Date(), 1), DATE_FORMAT);
        String verificationCode = randomAlphabetic(8).toUpperCase();
        try {
            jdbc.update(DELETE_VERIFICATION_CODE_BY_USER_ID, of("userId", user.getId()));
            jdbc.update(INSERT_VERIFICATION_CODE_QUERY, of("userId", user.getId(), "code", verificationCode, "expirationDate", expirationDate));
            //sendSMS(user.getPhone(), "From: SecureCapita \nVerification code\n" + verificationCode);
            log.info("Verification Code: {}", verificationCode);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again. ", exception);
        }
		
	}

	@Override
	public User verifyCode(String email, String code) {
		log.debug("verifyCode() email: " + email + " code: " + code);
		if(isVerificationCodeExpired(code)) throw new ApiException("This code has expired. Please login again.");
        try {
            User userByCode = jdbc.queryForObject(SELECT_USER_BY_USER_CODE_QUERY, of("code", code), new UserRowMapper());
            User userByEmail = jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, of("email", email), new UserRowMapper());
            if(userByCode.getEmail().equalsIgnoreCase(userByEmail.getEmail())) {
                jdbc.update(DELETE_CODE, of("code", code));
                return userByCode;
            } else {
                throw new ApiException("Code is invalid. Please try again.");
            }
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("Could not find record, ",exception);
        } catch (Exception exception) {
            throw new ApiException("An error occurred. Please try again. ",exception);
        }
	}

	@Override
	public void resetPassword(String email) {
		log.debug("resetPassword() email: " + email);
		if(getEmailCount(email.trim().toLowerCase()) <= 0) throw new ApiException("There is no account for this email address.");
        try {
                String expirationDate = format(addDays(new Date(), 1), DATE_FORMAT);
                User user = getUserByEmail(email);
                String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), PASSWORD.getType());
                jdbc.update(DELETE_PASSWORD_VERIFICATION_BY_USER_ID_QUERY, of("userId",  user.getId()));
                jdbc.update(INSERT_PASSWORD_VERIFICATION_QUERY, of("userId",  user.getId(), "url", verificationUrl, "expirationDate", expirationDate));
                // TODO send email with url to user
                log.info("Verification URL: {}", verificationUrl);
        } catch (Exception exception) {
            throw new ApiException("An error occurred. Please try again. ",exception);
        }
		
	}

	@Override
	public User verifyPasswordKey(String key) {
		log.debug("verifyPasswordKey() key: " + key);
		if(isLinkExpired(key, PASSWORD)) throw new ApiException("This link has expired. Please reset your password again.");
        try {
            User user = jdbc.queryForObject(SELECT_USER_BY_PASSWORD_URL_QUERY, of("url", getVerificationUrl(key, PASSWORD.getType())), new UserRowMapper());
            //jdbc.update("DELETE_USER_FROM_PASSWORD_VERIFICATION_QUERY", of("id", user.getId())); //Depends on use case / developer or business
            return user;
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new ApiException("This link is not valid, please reset your password again. ",exception);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred, please try again. ", exception);
        }
	}

	@Override
	public void renewPassword(String key, String password, String confirmPassword) {
		log.debug("renewPassword key: " + key + password + confirmPassword);
		if(!password.equals(confirmPassword)) throw new ApiException("Passwords don't match. Please try again.");
        try {
            jdbc.update(UPDATE_USER_PASSWORD_BY_URL_QUERY, of("password", encoder.encode(password), "url", getVerificationUrl(key, PASSWORD.getType())));
            jdbc.update(DELETE_VERIFICATION_BY_URL_QUERY, of("url", getVerificationUrl(key, PASSWORD.getType())));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred, please try again. ",exception);
        }
		
	}

	@Override
	public User verifyAccountKey(String key) {
		log.debug("verifyAccountKey() key: " + key);
		try {
            User user = jdbc.queryForObject(SELECT_USER_BY_ACCOUNT_URL_QUERY, of("url", getVerificationUrl(key, ACCOUNT.getType())), new UserRowMapper());
            jdbc.update(UPDATE_USER_ENABLED_QUERY, of("enabled", true, "userId", user.getId()));
            // Delete after updating - depends on your requirements
            return user;
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("This link is not valid. ",exception);
        } catch (Exception exception) {
            throw new ApiException("An error occurred, please try again. ",exception);
        }
	}

	@Override
	public User updateUserDetails(UpdateForm user) {
		log.debug("updateUserDetails() user: " + user);
		try {
            jdbc.update(UPDATE_USER_DETAILS_QUERY, getUserDetailsSqlParameterSource(user));
            return get(user.getId());
        }catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No User found by id: " + user.getId()+".",exception);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred, please try again. ",exception);
        }
	}

	@Override
	public void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
		log.debug("updatePassword() userId: " + id);
		if(!newPassword.equals(confirmNewPassword)) { throw new ApiException("Passwords don't match. Please try again."); }
        User user = get(id);
        if(encoder.matches(currentPassword, user.getPassword())) {
            try {
                jdbc.update(UPDATE_USER_PASSWORD_BY_ID_QUERY, of("userId", id, "password", encoder.encode(newPassword)));
            }  catch (Exception exception) {
                throw new ApiException("An error occurred, please try again. ",exception);
            }
        } else {
            throw new ApiException("Incorrect current password. Please try again.");
        }
		
	}

	@Override
	public void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked) {
		log.debug("updateAccountSettings() userId: " + userId);
		try {
            jdbc.update(UPDATE_USER_SETTINGS_QUERY, of("userId", userId, "enabled", enabled, "notLocked", notLocked));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred, please try again. ",exception);
        }
		
	}

	@Override
	public User toggleMfa(String email) {
		log.debug("toggleMfa() email: " + email);
		User user = getUserByEmail(email);
        if(isBlank(user.getPhone())) { throw new ApiException("You need a phone number to change Multi-Factor Authentication"); }
        user.setUsingMfa(!user.isUsingMfa());
        try {
            jdbc.update(TOGGLE_USER_MFA_QUERY, of("email", email, "isUsingMfa", user.isUsingMfa()));
            return user;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Unable to update Multi-Factor Authentication. ",exception);
        }
	}

	@Override
	public void updateImage(UserDTO user, MultipartFile image) {
		log.debug("updateImage() user: "+ user);
		try {
			String userImageUrl = setUserImageUrl(user.getEmail());
	        user.setImageUrl(userImageUrl);
	        saveImage(user.getEmail(), image);
	        jdbc.update(UPDATE_USER_IMAGE_QUERY, of("imageUrl", userImageUrl, "userId", user.getId()));
		} catch (Exception e) {
			log.error(e.getMessage());
            throw new ApiException("Unable to update Multi-Factor Authentication. ",e);
		}
		
		
	}
	
	private String setUserImageUrl(String email) {
		log.debug("-setUserImageUrl() email: "+ email);
        return fromCurrentContextPath().path("/user/image/" + email + ".png").toUriString();
    }

    private void saveImage(String email, MultipartFile image) {
    	log.debug("-saveImage() email: "+ email);
        Path fileStorageLocation = Paths.get(System.getProperty("user.home") + "/Downloads/images/").toAbsolutePath().normalize();
        if(!Files.exists(fileStorageLocation)) {
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new ApiException("Unable to create directories to save image.",exception);
            }
            log.info("Created directories: {}", fileStorageLocation);
        }
        try {
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(email + ".png"), REPLACE_EXISTING);
        } catch (IOException exception) {
            log.error(exception.getMessage());
            throw new ApiException("Oops ... failed to save image",exception);
        }
        log.info("File saved in: {} folder", fileStorageLocation);
    }

    private Boolean isLinkExpired(String key, VerificationType password) {
    	log.debug("-isLinkExpired() key: " + key + " verification type: " + password);
        try {
            return jdbc.queryForObject(SELECT_EXPIRATION_BY_URL, of("url", getVerificationUrl(key, password.getType())), Boolean.class);
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new ApiException("This link is not valid, please reset your password again.",exception);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred, please try again. ", exception);
        }
    }

    private Boolean isVerificationCodeExpired(String code) {
    	log.debug("-isVerificationCodeExpired() code: "+ code);
        try {
            return jdbc.queryForObject(SELECT_CODE_EXPIRATION_QUERY, of("code", code), Boolean.class);
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("This code is not valid. Please login again.");
        } catch (Exception exception) {
            throw new ApiException("An error occurred. Please try again.");
        }
    }
	
	private Integer getEmailCount(String email) {
		log.debug("-getEmailCount() email: " + email);
		return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email",email), Integer.class);
	}
	
	private SqlParameterSource getSqlParameterSource(User user) {
		log.debug("-getSqlParameterSource() user: " + user);
		return new MapSqlParameterSource()
				.addValue("firstName", user.getFirstName())
				.addValue("lastName", user.getLastName())
				.addValue("email", user.getEmail())
				.addValue("password", encoder.encode(user.getPassword()));
	}
	
	private SqlParameterSource getUserDetailsSqlParameterSource(UpdateForm user) {
		log.debug("-getUserDetailsSqlParameterSource() user: " + user);
        return new MapSqlParameterSource()
                .addValue("userId", user.getId())
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("phone", user.getPhone())
                .addValue("address", user.getAddress())
                .addValue("title", user.getTitle())
                .addValue("bio", user.getBio());
    }

	
	private String getVerificationUrl(String key, String type ) {
		log.debug("-getVerificationUrl() key: " + key + " type: "+ type);
		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/"+key).toUriString();
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.debug("loadUserByUsername() email: "+ email);
		User user = getUserByEmail(email);
        if(user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", email);
            return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()));
        }
	}
}
