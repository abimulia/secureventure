/**
 * 
 */
package com.abimulia.secureventure.user.repository;

import static com.abimulia.secureventure.user.enums.RoleType.ROLE_USER;
import static com.abimulia.secureventure.user.query.SqlQuery.*;

import static java.util.Map.of;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.abimulia.secureventure.exception.ApiException;
import com.abimulia.secureventure.user.domain.Role;
import com.abimulia.secureventure.user.mapper.RoleRowMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {
	private final NamedParameterJdbcTemplate jdbc;

	@Override
	public Role create(Role data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Role> list() {
		log.info("Fetching all roles");
		try {
			return jdbc.query(SELECT_ROLES_QUERY, new RoleRowMapper());
		} catch (Exception exception) {
			log.error(exception.getMessage());
			throw new ApiException("An error occurred. Please try again.");
		}
	}

	@Override
	public Role get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role update(Role data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRoleToUser(Long userId, String roleName) {
		log.debug("addRoleToUser() " + userId + " role: " + roleName);
		try {
			Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("roleName", roleName),
					new RoleRowMapper());
			log.debug("role: " + role);
			jdbc.update(INSERT_ROLE_TO_USER_QUERY,
					Map.of("userId", userId, "roleId", Objects.requireNonNull(role).getId()));
			log.info("Role {} added to user id: {}", roleName, userId);
		} catch (Exception e) {
			log.error("Error adding role to user, " + e.getMessage());
			throw new ApiException("Oops ... " + e.getMessage(),e);
		}

	}

	@Override
	public Role getRoleByUserId(Long userId) {
		log.info("Fetching role for user id: {}", userId);
		try {
			return jdbc.queryForObject(SELECT_ROLE_BY_ID_QUERY, of("id", userId), new RoleRowMapper());
		} catch (EmptyResultDataAccessException exception) {
			throw new ApiException("No role found for user id: " + userId,exception);
		} catch (Exception exception) {
			log.error(exception.getMessage());
			throw new ApiException("An error occurred. Please try again, "+exception.getMessage(),exception);
		}
	}

	@Override
	public Role getRoleByUserEmail(String email) {
		log.info("Fetching role for user with email: {}", email);
		try {
			return jdbc.queryForObject(SELECT_ROLE_BY_EMAIL_QUERY, of("email", email), new RoleRowMapper());
		} catch (EmptyResultDataAccessException exception) {
			throw new ApiException("No role found for user with email: " + email,exception);
		} catch (Exception exception) {
			log.error(exception.getMessage());
			throw new ApiException("An error occurred. Please try again, "+exception.getMessage(),exception);
		}
	}

	@Override
	public void updateUserRole(Long userId, String roleName) {
		log.info("Updating role for user id: {}", userId);
		try {
			Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, of("name", roleName), new RoleRowMapper());
			jdbc.update(UPDATE_USER_ROLE_QUERY, of("roleId", role.getId(), "userId", userId));
		} catch (EmptyResultDataAccessException exception) {
			throw new ApiException("No role found by name: " + roleName,exception);
		} catch (Exception exception) {
			log.error(exception.getMessage());
			throw new ApiException("Oops ... " + exception.getMessage(),exception);
		}

	}

}
