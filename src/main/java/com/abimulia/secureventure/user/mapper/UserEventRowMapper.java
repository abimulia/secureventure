/**
 * UserEventRowMapper.java
 * 26-Nov-2024
 */
package com.abimulia.secureventure.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.abimulia.secureventure.user.domain.UserEvent;

/**
* 
* @author abimu
*
* @version 1.0 (26-Nov-2024)
* @since 26-Nov-2024 10:56:48 PM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
public class UserEventRowMapper implements RowMapper<UserEvent>{

	@Override
	public UserEvent mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		return UserEvent.builder()
                .id(resultSet.getLong("userevent_id"))
                .type(resultSet.getString("type"))
                .description(resultSet.getString("description"))
                .device(resultSet.getString("device"))
                .ipAddress(resultSet.getString("ip_address"))
                .createdDate(resultSet.getTimestamp("created_date").toLocalDateTime())
                .build();
	}

}
