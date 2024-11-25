/**
 * 
 */
package com.abimulia.secureventure.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.abimulia.secureventure.user.domain.Role;

/**
 * 
 */
public class RoleRowMapper implements RowMapper<Role> {

	@Override
	public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
		return Role.builder()
				.id(rs.getLong("role_id"))
				.name(rs.getString("name"))
				.permission(rs.getString("permissions"))
				.build();
	}

}
