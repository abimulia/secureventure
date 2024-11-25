/**
 * 
 */
package com.abimulia.secureventure.user.repository;

import static com.abimulia.secureventure.enums.RoleType.ROLE_USER;
import static com.abimulia.secureventure.enums.VerificationType.ACCOUNT;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.abimulia.secureventure.exception.ApiException;
import com.abimulia.secureventure.user.domain.Role;
import com.abimulia.secureventure.user.mapper.RoleRowMapper;

import static com.abimulia.secureventure.user.query.SqlQuery.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role>{
	private final NamedParameterJdbcTemplate jdbc;

	@Override
	public Role create(Role data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Role> list(int page, int pageSize) {
		// TODO Auto-generated method stub
		return null;
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
			Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("roleName", roleName), new RoleRowMapper());
			log.debug("role: " + role);
			jdbc.update(INSERT_ROLE_TO_USER_QUERY, Map.of("userId", userId, "roleId",Objects.requireNonNull(role).getId()));
			log.info("Role {} added to user id: {}",roleName,userId);
		} 
		catch (Exception e) {
			log.error("Error adding role to user, " + e.getMessage());
			throw new ApiException("Oops, I did it again failed to add role to user, please try again.");
		}
		
	}

	@Override
	public Role getRoleByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role getRoleByUserEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUserRole(Long userId, String roleName) {
		// TODO Auto-generated method stub
		
	}

}
