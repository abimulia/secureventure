package com.abimulia.secureventure.user.repository;

import static com.abimulia.secureventure.enums.RoleType.ROLE_USER;
import static com.abimulia.secureventure.enums.VerificationType.ACCOUNT;
import static com.abimulia.secureventure.user.query.SqlQuery.COUNT_USER_EMAIL_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.INSERT_ACCOUNT_VERIFICATION_URL_QUERY;
import static com.abimulia.secureventure.user.query.SqlQuery.INSERT_USER_QUERY;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abimulia.secureventure.exception.ApiException;
import com.abimulia.secureventure.exception.UserAlreadyExistsException;
import com.abimulia.secureventure.user.domain.Role;
import com.abimulia.secureventure.user.domain.User;

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
public class UserRepositoryImpl implements UserRepository<User> {
	
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
			throw new ApiException(ROLE_USER.name()+ " Role not exists.");
		} 
		catch (Exception e) {
			log.error("Error creating user, " + e.getMessage());
			throw new ApiException("Oops, I did it again failed to create user, please try again.");
		}
	}

	@Override
	public Collection<User> list(int page, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User update(User data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	private Integer getEmailCount(String email) {
		return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email",email), Integer.class);
	}
	
	private SqlParameterSource getSqlParameterSource(User user) {
		return new MapSqlParameterSource()
				.addValue("firstName", user.getFirstName())
				.addValue("lastName", user.getLastName())
				.addValue("email", user.getEmail())
				.addValue("password", encoder.encode(user.getPassword()));
				
		
	}
	
	private String getVerificationUrl(String key, String type ) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/"+key).toUriString();
	}
}
