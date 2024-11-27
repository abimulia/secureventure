/**
 * UserRowMapper.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.abimulia.secureventure.user.domain.User;

/**
 * 
 * @author abimu
 *
 * @version 1.0 (26-Nov-2024)
 * @since 26-Nov-2024 9:28:01 PM
 * 
 * 
 *        Copyright(c) 2024 Abi Mulia
 */
public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		return User.builder().id(resultSet.getLong("user_id")).firstName(resultSet.getString("first_name"))
				.lastName(resultSet.getString("last_name")).email(resultSet.getString("email"))
				.password(resultSet.getString("password")).address(resultSet.getString("address"))
				.phone(resultSet.getString("phone")).title(resultSet.getString("title")).bio(resultSet.getString("bio"))
				.imageUrl(resultSet.getString("image_url")).enabled(resultSet.getBoolean("enabled"))
				.isUsingMfa(resultSet.getBoolean("using_mfa")).isNotLocked(resultSet.getBoolean("non_locked"))
				.createdDate(resultSet.getTimestamp("created_date").toLocalDateTime()).build();
	}

}
